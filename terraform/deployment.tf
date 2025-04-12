resource "kubernetes_deployment" "video_deployment" {
  metadata {
    name      = "fiap-hackathon-video-app"
    namespace = kubernetes_namespace.video_namespace.metadata[0].name
    labels = {
      app = "fiap-hackathon-video-app"
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "fiap-hackathon-video-app"
      }
    }

    template {
      metadata {
        labels = {
          app = "fiap-hackathon-video-app"
        }
      }

      spec {
        container {
          image             = data.aws_ecr_image.latest_image.image_uri
          name              = "fiap-hackathon-video-app"
          image_pull_policy = "Always"

          resources {
            limits = {
              cpu    = "500m"
              memory = "1Gi"
            }
            requests = {
              cpu    = "250m"
              memory = "512Mi"
            }
          }

          liveness_probe {
            http_get {
              path = "/video/actuator/health"
              port = var.server_port
            }
            initial_delay_seconds = 60
            period_seconds        = 30
            timeout_seconds       = 5
            failure_threshold     = 3
          }

          readiness_probe {
            http_get {
              path = "/video/actuator/health"
              port = var.server_port
            }
            initial_delay_seconds = 60
            period_seconds        = 10
            timeout_seconds       = 3
            failure_threshold     = 1
          }

          env {
            name  = "SPRING_PROFILES_ACTIVE"
            value = "default"
          }

          env {
            name = "SQS_QUEUE_VIDEO_UPLOAD_LISTENER"
            value = "video-create-queue"
          }

          env {
            name = "SQS_QUEUE_VIDEO_UPDATE_LISTENER"
            value = "video-update-queue"
          }

          env {
            name  = "SQS_QUEUE_VIDEO_PROCESS_PRODUCER"
            value = data.aws_sqs_queue.process_video_queue.url
          }

          env {
            name  = "SQS_QUEUE_NOTIFICATION_PUSH_PRODUCER"
            value = data.aws_sqs_queue.notification_push_queue.url
          }

          env {
            name = "VIDEO_FILE_S3_BUCKET"
            value = "video-file-store-${data.aws_caller_identity.current.account_id}"
          }

          env {
            name = "AWS_ACCESS_KEY_ID"
            value_from {
              secret_key_ref {
                name = "fiap-hackathon-video-secret"
                key  = "aws_access_key_id"
              }
            }
          }

          env {
            name = "AWS_SECRET_ACCESS_KEY"
            value_from {
              secret_key_ref {
                name = "fiap-hackathon-video-secret"
                key  = "aws_secret_access_key"
              }
            }
          }

          env {
            name = "AWS_SESSION_TOKEN"
            value_from {
              secret_key_ref {
                name = "fiap-hackathon-video-secret"
                key  = "aws_session_token"
              }
            }
          }

          env {
            name  = "AWS_REGION"
            value = "us-east-1"
          }

          env {
            name = "OTEL_SERVICE_NAME"
            value = "video-app"
          }

          env {
            name = "OTEL_EXPORTER_OTLP_ENDPOINT"
            value = var.otlp_endpoint
          }

          env {
            name = "OTEL_EXPORTER_OTLP_PROTOCOL"
            value = "grpc"
          }

          env {
            name = "OTEL_TRACES_EXPORTER"
            value = "otlp"
          }

          env {
            name  = "OTEL_METRICS_EXPORTER"
            value = "none"
          }

          env {
            name  = "OTEL_LOGS_EXPORTER"
            value = "otlp"
          }

          env {
            name = "OTEL_JAVAAGENT_DEBUG"
            value = "false"
          }

          env {
            name = "OTEL_JAVA_DISABLED_EXPORTERS"
            value = "logging"
          }

          env {
            name  = "OTEL_INSTRUMENTATION_SERVLET_EXCLUDE"
            value = "/video/actuator/health.*"
          }
        }
      }
    }
  }

  timeouts {
    create = "4m"
    update = "4m"
    delete = "4m"
  }

  depends_on = [kubernetes_secret.video_secret]
}
