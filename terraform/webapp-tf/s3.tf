resource "aws_s3_bucket_object" "artifact_upload" {
  bucket        = var.app_artifacts_s3_bucket
  key           = "${local.s3_app_artifact_name}.jar"
  source        = local.app_source_artifact
  etag          = filemd5(local.app_source_artifact)
//  etag          = filemd5("${path.module}/timestamp")
}
