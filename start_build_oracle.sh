#!/bin/bash
# BenchBase - Oracle TPC-C Build Script (Create tables and load data)

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="${SCRIPT_DIR}/tpcc_ora_build.xml"

echo "=========================================="
echo "BenchBase TPC-C Build for Oracle"
echo "=========================================="
echo "Config file: $CONFIG_FILE"
echo ""

# Include lib folder in classpath for Oracle JDBC driver
# Use semicolon for Windows, colon for Unix
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" || "$OSTYPE" == "win32" ]]; then
    java -cp "benchbase.jar;lib/*" com.oltpbenchmark.DBWorkload -b tpcc -c "$CONFIG_FILE" --create=true --load=true
else
    java -cp "benchbase.jar:lib/*" com.oltpbenchmark.DBWorkload -b tpcc -c "$CONFIG_FILE" --create=true --load=true
fi

echo ""
echo "Build completed."
