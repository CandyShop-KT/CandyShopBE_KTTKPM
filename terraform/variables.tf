variable "db_name" {
  description = "Name of the database"
  type        = string
  default     = "candyshop"
}
variable "db_username" {}
variable "db_password" {}
variable "aws_bucket" {}
variable "aws_access_key" {}
variable "aws_secret_key" {}
variable "redis_username" {}
variable "redis_password" {}
variable "smtp_host" {}
variable "smtp_port" {}
variable "smtp_username" {}
variable "smtp_password" {}
variable "jwt_secret" {}
variable "vnpay_merchant_id" {}
variable "vnpay_hash_secret" {}
variable "vnpay_payment_url" {}
variable "vnpay_return_url" {}