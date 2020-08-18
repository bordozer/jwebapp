resource "aws_cloudwatch_metric_alarm" "cpu_utilization_alarm" {
  alarm_name                = "${local.aws_service_name}-cpu-alarm"
  comparison_operator       = "GreaterThanOrEqualToThreshold"
  evaluation_periods        = "2"
  metric_name               = "CPUUtilization"
  namespace                 = "AWS/EC2"
  period                    = "120"
  statistic                 = "Average"
  threshold                 = "80"
  alarm_description         = "This metric monitors ec2 cpu utilization"
  insufficient_data_actions = []
}

resource "aws_cloudwatch_metric_alarm" "lb_healthy_hosts" {
  alarm_name          = "${local.aws_service_name}-healthy-hosts-alarm"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = "1"
  metric_name         = "HealthyHostCount"
  namespace           = "AWS/ApplicationELB"
  period              = "60"
  statistic           = "Average"
  threshold           = "1"
  alarm_description   = "Number of ${local.service_instance_name} nodes healthy in Target Group"
  actions_enabled     = "true"
  alarm_actions       = [data.aws_sns_topic.notification.arn]
  ok_actions          = [data.aws_sns_topic.notification.arn]
  dimensions = {
    TargetGroup  = aws_lb_target_group.lb_tg.arn_suffix
    LoadBalancer = aws_lb.front_end.arn_suffix
  }
}
