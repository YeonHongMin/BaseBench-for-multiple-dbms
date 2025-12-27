#!/bin/bash
# BenchBase - Tibero TPC-C Drop Script (Clear tables)

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="${SCRIPT_DIR}/tpcc_tibero_build.xml"

echo "=========================================="
echo "BenchBase TPC-C Drop Tables for Tibero"
echo "=========================================="
echo "Config file: $CONFIG_FILE"
echo ""

# Include lib folder in classpath for Tibero JDBC driver
# Use semicolon for Windows, colon for Unix
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" || "$OSTYPE" == "win32" ]]; then
    java -cp "benchbase.jar;lib/*" com.oltpbenchmark.DBWorkload -b tpcc -c "$CONFIG_FILE" --clear=true
else
    java -cp "benchbase.jar:lib/*" com.oltpbenchmark.DBWorkload -b tpcc -c "$CONFIG_FILE" --clear=true
fi

echo ""
echo "Tables dropped."
