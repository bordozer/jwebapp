resource "aws_instance" "ec2_instance" {

  instance_type = var.instance_type
  ami = var.ami_id

  root_block_device {
    volume_type = var.ec2_instance_root_volume_type
    volume_size = var.ec2_instance_root_volume_size
  }

  monitoring = false
  vpc_security_group_ids = [aws_security_group.ec2_sg.id]
  availability_zone = element(var.availability_zones, 0)
  subnet_id = element(var.subnets, 0)
  associate_public_ip_address = true
  iam_instance_profile = aws_iam_instance_profile.instance_profile.id

  key_name = aws_key_pair.ssh_key.id

  lifecycle {
    create_before_destroy = false
  }
  user_data = data.template_file.ec2_userdata.rendered

  tags = local.common_tags
}

resource "aws_lb_target_group_attachment" "ec2_attach" {
  target_group_arn = aws_lb_target_group.lb_tg.arn
  target_id        = aws_instance.ec2_instance.id
  port             = var.app_port
}
