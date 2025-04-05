resource "kubernetes_horizontal_pod_autoscaler_v2" "video_hpa" {
  metadata {
    name      = "fiap-hackathon-video-app-hpa"
    namespace = kubernetes_namespace.video_namespace.metadata[0].name
  }

  spec {
    scale_target_ref {
      api_version = "apps/v1"
      kind        = "Deployment"
      name        = "fiap-hackathon-video-app"
    }

    min_replicas = 1
    max_replicas = 5

    metric {
      type = "Resource"

      resource {
        name = "cpu"
        target {
          type                = "Utilization"
          average_utilization = 75
        }
      }
    }
  }

  depends_on = [kubernetes_service.video_service]
}
