resource "kubernetes_ingress_v1" "video_ingress" {
  metadata {
    name      = "fiap-hackathon-video-app-ingress"
    namespace = kubernetes_namespace.video_namespace.metadata[0].name

    annotations = {
      "nginx.ingress.kubernetes.io/x-forwarded-port" = "true"
      "nginx.ingress.kubernetes.io/x-forwarded-host" = "true"
    }
  }

  spec {
    ingress_class_name = "nginx"

    rule {
      http {
        path {
          path      = "/video"
          path_type = "Prefix"

          backend {
            service {
              name = "fiap-hackathon-video-app-service"
              port {
                number = var.server_port
              }
            }
          }
        }
      }
    }
  }

  depends_on = [kubernetes_service.video_service]

}
