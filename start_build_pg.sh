#!/bin/bash
# BenchBase - PostgreSQL TPC-C Build Script (Create tables and load data)

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="${SCRIPT_DIR}/tpcc_pg_build.xml"

echo "=========================================="
echo "BenchBase TPC-C Build for PostgreSQL"
echo "=========================================="
echo "Config file: $CONFIG_FILE"
echo ""

java -jar benchbase.jar -b tpcc -c "$CONFIG_FILE" --create=true --load=true

echo ""
echo "Build completed."
