resource "google_secret_manager_secret" "db_password" {
  secret_id = "db-password"
  replication {
    user_managed {
      replicas {
        location = "asia-southeast1"
      }
    }
  }
}

resource "google_secret_manager_secret_version" "db_password" {
  secret = google_secret_manager_secret.db_password.id
  secret_data = var.db_password
}

resource "google_secret_manager_secret" "aws_credentials" {
  secret_id = "aws-credentials"
  replication {
    user_managed {
      replicas {
        location = "asia-southeast1"
      }
    }
  }
}

resource "google_secret_manager_secret_version" "aws_credentials" {
  secret = google_secret_manager_secret.aws_credentials.id
  secret_data = jsonencode({
    access_key = var.aws_access_key
    secret_key = var.aws_secret_key
  })
}

resource "google_secret_manager_secret" "redis_credentials" {
  secret_id = "redis-credentials"
  replication {
    user_managed {
      replicas {
        location = "asia-southeast1"
      }
    }
  }
}

resource "google_secret_manager_secret_version" "redis_credentials" {
  secret = google_secret_manager_secret.redis_credentials.id
  secret_data = jsonencode({
    username = var.redis_username
    password = var.redis_password
  })
}

resource "google_secret_manager_secret" "smtp_credentials" {
  secret_id = "smtp-credentials"
  replication {
    user_managed {
      replicas {
        location = "asia-southeast1"
      }
    }
  }
}

resource "google_secret_manager_secret_version" "smtp_credentials" {
  secret = google_secret_manager_secret.smtp_credentials.id
  secret_data = jsonencode({
    host     = var.smtp_host
    port     = var.smtp_port
    username = var.smtp_username
    password = var.smtp_password
  })
}

resource "google_secret_manager_secret" "jwt_secret" {
  secret_id = "jwt-secret"
  replication {
    user_managed {
      replicas {
        location = "asia-southeast1"
      }
    }
  }
}

resource "google_secret_manager_secret_version" "jwt_secret" {
  secret = google_secret_manager_secret.jwt_secret.id
  secret_data = var.jwt_secret
}

resource "google_secret_manager_secret" "vnpay_credentials" {
  secret_id = "vnpay-credentials"
  replication {
    user_managed {
      replicas {
        location = "asia-southeast1"
      }
    }
  }
}

resource "google_secret_manager_secret_version" "vnpay_credentials" {
  secret = google_secret_manager_secret.vnpay_credentials.id
  secret_data = jsonencode({
    merchant_id   = var.vnpay_merchant_id
    hash_secret   = var.vnpay_hash_secret
    payment_url   = var.vnpay_payment_url
    return_url    = var.vnpay_return_url
  })
} 