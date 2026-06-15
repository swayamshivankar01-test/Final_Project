#!/bin/bash
# =============================================================================
# run-newman.sh
# Run Postman collection via Newman and generate HTML + JSON reports
# =============================================================================

set -e

COLLECTION="DummyJSON_nopCommerce_QA_Capstone_API_Tests_FINAL_postman_collection.json"
ENVIRONMENT="DummyJSON_nopCommerce_QA_Capstone_Environment.postman_environment.json"
REPORTS_DIR="newman-reports"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

echo "============================================"
echo "  DummyJSON nopCommerce QA — Newman Run"
echo "============================================"

# ---------- Pre-flight checks ----------
if ! command -v node &>/dev/null; then
  echo "[ERROR] Node.js is not installed. Install it from https://nodejs.org"
  exit 1
fi

if ! command -v newman &>/dev/null; then
  echo "[INFO] Newman not found — installing globally..."
  npm install -g newman newman-reporter-htmlextra
fi

if ! newman --version &>/dev/null; then
  echo "[ERROR] Newman installation failed."
  exit 1
fi

echo "[INFO] Newman version: $(newman --version)"

# ---------- Ensure report directory exists ----------
mkdir -p "$REPORTS_DIR"

# ---------- Run Newman ----------
echo ""
echo "[INFO] Running collection: $COLLECTION"
echo "[INFO] Environment:        $ENVIRONMENT"
echo ""

newman run "$COLLECTION" \
  --environment "$ENVIRONMENT" \
  --reporters cli,json,htmlextra \
  --reporter-json-export  "$REPORTS_DIR/newman-report-${TIMESTAMP}.json" \
  --reporter-htmlextra-export "$REPORTS_DIR/newman-report-${TIMESTAMP}.html" \
  --reporter-htmlextra-title  "DummyJSON nopCommerce QA Capstone" \
  --reporter-htmlextra-darkTheme \
  --reporter-htmlextra-showOnlyFails false \
  --reporter-htmlextra-logs \
  --timeout-request 10000 \
  --delay-request 200 \
  --color on \
  || NEWMAN_EXIT=$?

echo ""
echo "============================================"
echo "  Newman run complete"
echo "  HTML Report : $REPORTS_DIR/newman-report-${TIMESTAMP}.html"
echo "  JSON Report : $REPORTS_DIR/newman-report-${TIMESTAMP}.json"
echo "============================================"

# Exit with Newman's exit code (non-zero = test failures)
exit ${NEWMAN_EXIT:-0}
