resource "aws_iam_role" "service_iam_role" {
  name = "${local.aws_service_name}-iam-role"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF

  tags = local.common_tags
}

resource "aws_iam_role_policy" "service_s3_access_policy" {
  name = "${local.aws_service_name}-role-policy"
  role = aws_iam_role.service_iam_role.id
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:Get*",
                "s3:List*"
            ],
            "Resource": "*"
        }
    ]
}
EOF
}

resource "aws_iam_role_policy" "cloud_watch_logs_policy" {
  name = "${local.aws_service_name}-cloud-watch-logs-policy"
  role = aws_iam_role.service_iam_role.id
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "logs:*"
            ],
            "Effect": "Allow",
            "Resource": "*"
        }
    ]
}
EOF
}

resource "aws_iam_role_policy" "get_parameter_policy" {
  name = "${local.aws_service_name}-get-parameter-store-policy"
  role = aws_iam_role.service_iam_role.id
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "ssm:GetParametersByPath"
            ],
            "Effect": "Allow",
            "Resource": "*"
        }
    ]
}
EOF
}
