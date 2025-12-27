#!/bin/bash
# BenchBase - DB2 TPC-C Build Script (Create tables and load data)

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="${SCRIPT_DIR}/tpcc_db2_build.xml"

echo "=========================================="
echo "BenchBase TPC-C Build for DB2"
echo "=========================================="
echo "Config file: $CONFIG_FILE"
echo ""

java -jar benchbase.jar -b tpcc -c "$CONFIG_FILE" --create=true --load=true

echo ""
echo "Build completed."
