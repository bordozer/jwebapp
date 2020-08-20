resource "aws_iam_instance_profile" "instance_profile" {
  name = "${local.aws_service_name}-instance-profile"
  role = aws_iam_role.ec2_iam_role.name
}

data "template_file" "ec2_userdata" {
  template = file("user_data.sh")
  vars = {
    t_service_name = var.service_name
    t_service_instance_name = local.service_instance_name
    t_env = var.environment
    t_app_dir = "/opt/${local.service_instance_name}"
    t_app_dev_db_dir = "/opt/dev-db"
    t_app_artifact_s3_bucket = var.app_artifacts_s3_bucket
    t_app_artifact_name = local.s3_app_artifact_name
    t_region = var.aws_region
    t_log_group = aws_cloudwatch_log_group.app_logs.name
    t_source_code_hash = local.source_code_hash
  }
}
