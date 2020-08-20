resource "aws_s3_bucket_object" "artifact_upload" {
  bucket        = var.app_artifacts_s3_bucket
  key           = "${local.s3_app_artifact_name}.jar"
  source        = local.app_source_artifact
  etag          = filemd5(local.app_source_artifact)

  tags = {
    Name            = local.service_instance_name
    ServiceName     = var.service_name
    Environment     = var.environment
    CreatedBy       = "Terraform"
    GitRepoName     = var.git_repo_name
    GitRepo         = var.git_repo_name
    GitBranch       = var.git_branch
    GitHash         = var.git_hash
  }
}
