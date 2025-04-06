resource "kubernetes_service" "video_service" {
  metadata {
    name      = "fiap-hackathon-video-app-service"
    namespace = kubernetes_namespace.video_namespace.metadata[0].name
  }

  spec {
    selector = {
      app = "fiap-hackathon-video-app"
    }

    port {
      port        = var.server_port
      target_port = var.server_port
    }

    cluster_ip = "None"
  }
}
