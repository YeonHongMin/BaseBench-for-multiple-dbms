#!/bin/bash
# BenchBase - MySQL TPC-C Build Script (Create tables and load data)

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="${SCRIPT_DIR}/tpcc_mysql_build.xml"

echo "=========================================="
echo "BenchBase TPC-C Build for MySQL"
echo "=========================================="
echo "Config file: $CONFIG_FILE"
echo ""

java -jar benchbase.jar -b tpcc -c "$CONFIG_FILE" --create=true --load=true

echo ""
echo "Build completed."
