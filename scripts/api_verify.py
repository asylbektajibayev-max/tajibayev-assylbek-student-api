#!/usr/bin/env python3
"""Real HTTP assertions; save responses, do not fabricate evidence."""
import json, os, time, urllib.request, urllib.error, pathlib
base=os.getenv('API_URL','http://127.0.0.1:18080')
name=os.getenv('STUDENT_FULL_NAME','Tajibayev Assylbek')
out=[]
def request(method,path,data=None,expected=200):
 raw=None if data is None else json.dumps(data).encode()
 req=urllib.request.Request(base+path,data=raw,method=method,headers={'Content-Type':'application/json'})
 try:
  with urllib.request.urlopen(req,timeout=20) as r: status,body=r.status,r.read().decode()
 except urllib.error.HTTPError as e: status,body=e.code,e.read().decode()
 record={'method':method,'url':base+path,'status':status,'response':body}
 out.append(record); print(json.dumps(record,ensure_ascii=False))
 assert status==expected,(status,expected,body)
 return json.loads(body) if body else None
for _ in range(90):
 try: urllib.request.urlopen(base+'/actuator/health',timeout=2); break
 except (OSError,urllib.error.URLError): time.sleep(2)
else: raise RuntimeError('API did not become healthy')
try:
 data={'name':name,'email':f'student-{time.time_ns()}@example.org','course':'DevOps'}
 s=request('POST','/api/students',data,201); path=f"/api/students/{s['id']}"
 request('GET','/api/students'); request('GET',path)
 data['course']='DevOps updated'; updated=request('PUT',path,data); assert updated['course']==data['course']
 request('POST','/api/students',data,409)
 request('POST','/api/students',{'name':'','email':'bad','course':''},400)
 request('DELETE',path,expected=204); request('GET',path,expected=404)
 request('PUT',path,data,404); request('DELETE',path,expected=404)
finally:
 pathlib.Path('evidence').mkdir(exist_ok=True)
 pathlib.Path('evidence/api-responses.json').write_text(json.dumps(out,ensure_ascii=False,indent=2),encoding='utf-8')
