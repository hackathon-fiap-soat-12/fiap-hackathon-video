data "aws_caller_identity" "current" {}

data "aws_eks_cluster" "eks_cluster" {
  name = var.eks_cluster_name
}

data "aws_eks_cluster_auth" "eks_cluster" {
  name = data.aws_eks_cluster.eks_cluster.name
}

data "aws_ecr_repository" "ecr_repo" {
  name = var.ecr_repository_name
}

data "aws_ecr_image" "latest_image" {
  repository_name = data.aws_ecr_repository.ecr_repo.name
  image_tag       = "latest"
}

data "aws_sqs_queue" "notification_push_queue" {
  name = "notification-push-queue"
}

data "aws_sqs_queue" "process_video_queue" {
  name = "process-video-queue"
}
