#!/bin/bash
# BenchBase - PostgreSQL TPC-C Execute Script (Run benchmark)

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_FILE="${SCRIPT_DIR}/tpcc_pg_build.xml"
OUTPUT_DIR="${SCRIPT_DIR}/results/tpcc_pg"

mkdir -p "$OUTPUT_DIR"

echo "=========================================="
echo "BenchBase TPC-C Execution for PostgreSQL"
echo "=========================================="
echo "Config file: $CONFIG_FILE"
echo "Output directory: $OUTPUT_DIR"
echo ""

java -jar benchbase.jar -b tpcc -c "$CONFIG_FILE" --execute=true -s 5 -d "$OUTPUT_DIR"

echo ""
echo "Execution completed. Results saved to $OUTPUT_DIR"
