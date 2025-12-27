#!/bin/bash
# BenchBase - DB2 TPC-C Drop Script (Clear tables)

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="${SCRIPT_DIR}/tpcc_db2_build.xml"

echo "=========================================="
echo "BenchBase TPC-C Drop Tables for DB2"
echo "=========================================="
echo "Config file: $CONFIG_FILE"
echo ""

java -jar benchbase.jar -b tpcc -c "$CONFIG_FILE" --clear=true

echo ""
echo "Tables dropped."
