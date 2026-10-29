#!/bin/bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
source "$ROOT/student.conf"
mkdir -p "$ROOT/logs"
{
  echo "Student: $STUDENT_FULL_NAME"
  echo "Date: $(date --iso-8601=seconds)"
  echo "Username: $(whoami)"
  echo 'Operating system:'
  cat /etc/os-release
  echo "Kernel: $(uname -r)"
  echo 'CPU:'
  lscpu
  echo 'Memory:'
  free -h
  echo 'Disk usage and available space:'
  df -h
  echo 'Project disk usage:'
  du -sh "$ROOT"
} | tee "$ROOT/logs/${STUDENT_FILE_ID}_system_report.log"
