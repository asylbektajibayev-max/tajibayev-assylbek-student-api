#!/bin/bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"
source student.conf
mkdir -p scripts logs backups src evidence
touch "logs/${STUDENT_FILE_ID}_system_report.log"
chmod 744 "scripts/${STUDENT_FILE_ID}_system_report.sh"
cp README.md "backups/README_${STUDENT_FILE_ID}.md"
{
  echo '=== Directory structure ==='
  find . -path './.git' -prune -o -path './.gradle' -prune -o -path './build' -prune -o -print | sort
  echo '=== Required files ==='
  for file in Dockerfile docker-compose.yml Jenkinsfile build.gradle README.md; do test -f "$file"; ls -l "$file"; done
  echo '=== Permissions ==='
  ls -l "scripts/${STUDENT_FILE_ID}_system_report.sh"
  echo '=== Report ==='
  "scripts/${STUDENT_FILE_ID}_system_report.sh"
  echo '=== Backup verification ==='
  cmp README.md "backups/README_${STUDENT_FILE_ID}.md"
  ls -l "backups/README_${STUDENT_FILE_ID}.md"
  cat "backups/README_${STUDENT_FILE_ID}.md"
  echo '=== Processes ==='
  ps aux
  echo '=== Java processes ==='
  ps -eo pid,comm,args | awk '$2 == "java" {found=1; print} END {if (!found) print "No Java process is running in this environment"}'
  echo '=== Last five log lines ==='
  tail -n 5 "logs/${STUDENT_FILE_ID}_system_report.log"
  echo '=== Surname search ==='
  grep -F "$STUDENT_SURNAME" "logs/${STUDENT_FILE_ID}_system_report.log" > "logs/${STUDENT_FILE_ID}_log_analysis.txt"
  cat "logs/${STUDENT_FILE_ID}_log_analysis.txt"
  echo '> creates or overwrites a file; >> creates or appends to a file.'
} 2>&1 | tee evidence/linux-terminal.txt
