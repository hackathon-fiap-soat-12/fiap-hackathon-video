resource "kubernetes_secret" "video_secret" {
  metadata {
    name      = "fiap-hackathon-video-secret"
    namespace = kubernetes_namespace.video_namespace.metadata[0].name
  }

  data = {
    aws_access_key_id     = var.aws_access_key_id
    aws_secret_access_key = var.aws_secret_access_key
    aws_session_token     = var.aws_session_token
    aws_region            = var.aws_region
  }

  depends_on = [kubernetes_namespace.video_namespace]
}
