# Cloud Monitoring alert policy for high error rate
resource "google_monitoring_alert_policy" "cloud_run_errors" {
  display_name = "Cloud Run High Error Rate"
  combiner     = "OR"
  conditions {
    display_name = "Error rate is high"
    condition_threshold {
      filter     = "resource.type = \"cloud_run_revision\" AND metric.type = \"run.googleapis.com/request_count\""
      duration   = "300s"
      comparison = "COMPARISON_GT"
      threshold_value = 10
      trigger {
        count = 1
      }
    }
  }

  notification_channels = [google_monitoring_notification_channel.email.id]
}

# Email notification channel
resource "google_monitoring_notification_channel" "email" {
  display_name = "Email Alerts"
  type         = "email"
  labels = {
    email_address = "your-email@example.com"  # Thay đổi email này
  }
}

# Cloud Monitoring alert policy for high latency
resource "google_monitoring_alert_policy" "cloud_run_latency" {
  display_name = "Cloud Run High Latency"
  combiner     = "OR"
  conditions {
    display_name = "Latency is high"
    condition_threshold {
      filter     = "resource.type = \"cloud_run_revision\" AND metric.type = \"run.googleapis.com/request_latencies\""
      duration   = "300s"
      comparison = "COMPARISON_GT"
      threshold_value = 1000  # 1 second
      trigger {
        count = 1
      }
    }
  }

  notification_channels = [google_monitoring_notification_channel.email.id]
}

# Cloud Monitoring alert policy for high memory usage
resource "google_monitoring_alert_policy" "cloud_run_memory" {
  display_name = "Cloud Run High Memory Usage"
  combiner     = "OR"
  conditions {
    display_name = "Memory usage is high"
    condition_threshold {
      filter     = "resource.type = \"cloud_run_revision\" AND metric.type = \"run.googleapis.com/container/memory/utilizations\""
      duration   = "300s"
      comparison = "COMPARISON_GT"
      threshold_value = 0.8  # 80%
      trigger {
        count = 1
      }
    }
  }

  notification_channels = [google_monitoring_notification_channel.email.id]
} 