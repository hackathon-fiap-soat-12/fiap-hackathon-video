resource "kubernetes_namespace" "video_namespace" {
  metadata {
    name = "video"
  }
}
