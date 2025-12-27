#!/bin/bash
# BenchBase - Oracle TPC-C Execute Script (Run benchmark)

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="${SCRIPT_DIR}/tpcc_ora_build.xml"
OUTPUT_DIR="${SCRIPT_DIR}/results/tpcc_oracle"

mkdir -p "$OUTPUT_DIR"

echo "=========================================="
echo "BenchBase TPC-C Execution for Oracle"
echo "=========================================="
echo "Config file: $CONFIG_FILE"
echo "Output directory: $OUTPUT_DIR"
echo ""

# Include lib folder in classpath for Oracle JDBC driver
# Use semicolon for Windows, colon for Unix
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" || "$OSTYPE" == "win32" ]]; then
    java -cp "benchbase.jar;lib/*" com.oltpbenchmark.DBWorkload -b tpcc -c "$CONFIG_FILE" --execute=true -s 5 -d "$OUTPUT_DIR"
else
    java -cp "benchbase.jar:lib/*" com.oltpbenchmark.DBWorkload -b tpcc -c "$CONFIG_FILE" --execute=true -s 5 -d "$OUTPUT_DIR"
fi

echo ""
echo "Execution completed. Results saved to $OUTPUT_DIR"
