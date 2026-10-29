#!/usr/bin/env python3
"""Create local credentials once; never overwrite an existing environment."""
from pathlib import Path
import secrets
root=Path(__file__).resolve().parent.parent
target=root/'.env'
if target.exists():
    print('.env already exists; unchanged')
else:
    text=(root/'.env.example').read_text(encoding='utf-8')
    text=text.replace('replace-with-a-random-local-password',secrets.token_urlsafe(32))
    with target.open('x',encoding='utf-8') as f:
        f.write(text)
    target.chmod(0o600)
    print('Local .env created; password not displayed')
