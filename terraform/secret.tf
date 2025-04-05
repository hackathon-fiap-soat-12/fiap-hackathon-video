resource "kubernetes_secret" "video_secret" {
  metadata {
    name      = "fiap-hackathon-video-secret"
    namespace = kubernetes_namespace.video_namespace.metadata[0].name
  }

  data = {
    AWS_ACCESS_KEY_ID     = var.aws_access_key_id
    AWS_SECRET_ACCESS_KEY = var.aws_secret_access_key
    AWS_SESSION_TOKEN     = var.aws_session_token
    AWS_REGION            = var.aws_region
  }

  depends_on = [kubernetes_namespace.video_namespace]
}