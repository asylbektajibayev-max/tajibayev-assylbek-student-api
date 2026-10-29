#!/usr/bin/env python3
"""Run the assignment's API demonstration with the real curl executable."""
import json, os, pathlib, shutil, subprocess, tempfile, time
root=pathlib.Path(__file__).resolve().parent.parent
base=os.getenv('API_URL','http://127.0.0.1:18080')
curl=shutil.which('curl.exe') or shutil.which('curl')
if not curl: raise SystemExit('curl is required')
records=[]
def run(method,path,payload=None,expected=200):
 with tempfile.TemporaryDirectory(prefix='student-api-') as tmp:
  response=pathlib.Path(tmp)/'response.json'
  command=[curl,'--silent','--show-error','--max-time','20','-X',method,'-o',str(response),'-w','%{http_code}',base+path]
  if payload is not None:
   data=pathlib.Path(tmp)/'request.json'; data.write_text(json.dumps(payload),encoding='utf-8')
   command+=['-H','Content-Type: application/json','--data-binary','@'+str(data)]
  r=subprocess.run(command,capture_output=True,text=True,check=True)
  body=response.read_text(encoding='utf-8')
  status=int(r.stdout)
  records.append({'command':f'curl -X {method} {base+path}','request':payload,'status':status,'response':body})
  print(json.dumps(records[-1],ensure_ascii=False))
  assert status==expected,(status,expected,body)
  return json.loads(body) if body else None
try:
 student={'name':'Tajibayev Assylbek','email':f'curl-{time.time_ns()}@example.org','course':'DevOps IT2 2310'}
 created=run('POST','/api/students',student,201)
 path='/api/students/'+str(created['id'])
 run('GET','/api/students'); run('GET',path)
 student['course']='DevOps 38288 updated'; run('PUT',path,student)
 run('DELETE',path,expected=204); run('GET',path,expected=404)
finally:
 (root/'evidence').mkdir(exist_ok=True)
 (root/'evidence/curl-api-demonstration.json').write_text(json.dumps(records,ensure_ascii=False,indent=2),encoding='utf-8')
