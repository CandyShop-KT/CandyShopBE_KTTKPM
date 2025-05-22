provider "google" {
  project = "candy-shop-460008"
  region  = "asia-southeast1"
}

resource "google_cloud_run_service" "app" {
  name     = "candyshop-backend"
  location = "asia-southeast1"
  template {
    spec {
      containers {
  image = "gcr.io/candy-shop-460008/candyshop:latest"
  env {
    name  = "DB_USERNAME"
    value = var.db_username
  }
  env {
    name  = "DB_PASSWORD"
    value = var.db_password
  }
  env {
    name  = "AWS_BUCKET"
    value = var.aws_bucket
  }
  env {
    name  = "AWS_ACCESS_KEY"
    value = var.aws_access_key
  }
  env {
    name  = "AWS_SECRET_KEY"
    value = var.aws_secret_key
  }
  env {
    name  = "REDIS_USERNAME"
    value = var.redis_username
  }
  env {
    name  = "REDIS_PASSWORD"
    value = var.redis_password
  }
  env {
    name  = "SMTP_HOST"
    value = var.smtp_host
  }
  env {
    name  = "SMTP_PORT"
    value = var.smtp_port
  }
  env {
    name  = "SMTP_USERNAME"
    value = var.smtp_username
  }
  env {
    name  = "SMTP_PASSWORD"
    value = var.smtp_password
  }
  env {
    name  = "JWT_SECRET"
    value = var.jwt_secret
  }
  env {
    name  = "VNPAY_MERCHANT_ID"
    value = var.vnpay_merchant_id
  }
  env {
    name  = "VNPAY_HASH_SECRET"
    value = var.vnpay_hash_secret
  }
  env {
    name  = "VNPAY_PAYMENT_URL"
    value = var.vnpay_payment_url
  }
  env {
    name  = "VNPAY_RETURN_URL"
    value = var.vnpay_return_url
  }
  # Thêm các biến khác nếu cần
}
    }
  }
}