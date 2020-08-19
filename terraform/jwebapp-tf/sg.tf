resource "aws_security_group" "lb_sg" {
  name = "${local.aws_service_name}-alb-sg"
  description = "${local.aws_service_name} ALB SG"

  vpc_id = var.vpc

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    description = "TLS from VPC"
    cidr_blocks = [ "0.0.0.0/0" ]
  }

  # Access from LB to everywhere
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = [ "0.0.0.0/0" ]
  }

  tags = local.common_tags
}

resource "aws_security_group" "ec2_sg" {
  name = "${local.aws_service_name}-ec2-sg"
  description = "${local.service_instance_name} EC2 SG"

  vpc_id = var.vpc

  # Egress connections to Internet from Security Group
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = [ "0.0.0.0/0" ]
  }

  tags = local.common_tags
}

resource "aws_security_group_rule" "ec2_sg_rule_ssh" {
  security_group_id = aws_security_group.ec2_sg.id
  type              = "ingress"
  from_port         = 22
  to_port           = 22
  protocol          = "tcp"
  cidr_blocks       = [ "0.0.0.0/0" ]
  description       = "SSH access"
}

/* HTTP from LB */
resource "aws_security_group_rule" "ec2_sg_rule_http" {
  security_group_id = aws_security_group.ec2_sg.id
  type            = "ingress"
  from_port       = var.app_port
  to_port         = var.app_port
  protocol        = "tcp"
  source_security_group_id = aws_security_group.lb_sg.id
  description       = "LB access to webapp on EC2"
}
