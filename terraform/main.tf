provider "google" {
  project = "candy-shop-460008"
  region  = "asia-southeast1"
}

resource "google_cloud_run_service" "app" {
  name     = "candyshop-backend"
  location = "asia-southeast1"
  
  template {
    spec {
      service_account_name = google_service_account.cloud_run_sa.email
      
      containers {
        image = "gcr.io/candy-shop-460008/candyshop:latest"
        
        resources {
          limits = {
            cpu    = "1000m"
            memory = "512Mi"
          }
        }
        
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
      }
    }
  }
  
  traffic {
    percent         = 100
    latest_revision = true
  }
}

# Cloud Storage bucket for application assets
resource "google_storage_bucket" "app_assets" {
  name          = "candyshop-assets"
  location      = "asia-southeast1"
  force_destroy = true

  uniform_bucket_level_access = true
}

# Cloud SQL instance for database
resource "google_sql_database_instance" "main" {
  name             = "candyshop-db"
  database_version = "MYSQL_8_0"
  region           = "asia-southeast1"

  settings {
    tier = "db-f1-micro"
    
    backup_configuration {
      enabled = true
    }
    
    ip_configuration {
      ipv4_enabled = true
    }
  }
}

# Cloud SQL database
resource "google_sql_database" "database" {
  name     = "candyshop"
  instance = google_sql_database_instance.main.name
}

# Cloud SQL user
resource "google_sql_user" "user" {
  name     = var.db_username
  instance = google_sql_database_instance.main.name
  password = var.db_password
}