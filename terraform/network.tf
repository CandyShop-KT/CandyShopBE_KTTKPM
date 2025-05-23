# VPC Network
resource "google_compute_network" "vpc" {
  name                    = "candyshop-vpc"
  auto_create_subnetworks = false
  project                 = "candy-shop-460008"
}

# Subnet
resource "google_compute_subnetwork" "subnet" {
  name          = "candyshop-subnet"
  ip_cidr_range = "10.0.0.0/24"
  network       = google_compute_network.vpc.id
  region        = "asia-southeast1"
  project       = "candy-shop-460008"
}

# Firewall rule for Cloud Run
resource "google_compute_firewall" "allow_cloud_run" {
  name    = "allow-cloud-run"
  network = google_compute_network.vpc.name
  project = "candy-shop-460008"

  allow {
    protocol = "tcp"
    ports    = ["80", "443"]
  }

  source_ranges = ["0.0.0.0/0"]
  target_tags   = ["cloud-run"]
}

# Cloud NAT for outbound traffic
resource "google_compute_router" "router" {
  name    = "candyshop-router"
  region  = "asia-southeast1"
  network = google_compute_network.vpc.id
  project = "candy-shop-460008"
}

resource "google_compute_router_nat" "nat" {
  name                               = "candyshop-nat"
  router                            = google_compute_router.router.name
  region                            = google_compute_router.router.region
  nat_ip_allocate_option            = "AUTO_ONLY"
  source_subnetwork_ip_ranges_to_nat = "ALL_SUBNETWORKS_ALL_IP_RANGES"
  project                           = "candy-shop-460008"
} 