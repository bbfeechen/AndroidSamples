#!/usr/bin/env python

# coding: UTF-8

import sys
import subprocess

CMD_TEMPLATE1 = 'grep -R -s -q R.string.%s src'
CMD_TEMPLATE2 = 'grep -R -s -q @string/%s res AndroidManifest.xml'
CMD_TEMPLATE3 = 'grep -R -s -q %s assets/DLAppCautionSample.db'

CMD_LIST = [CMD_TEMPLATE1, CMD_TEMPLATE2, CMD_TEMPLATE3]

# ----------------------------------------
ID_LIST = []
sys.argv.pop(0)
for file in sys.argv:
  for str_id in open(file, 'r'):
    str_id = str_id.strip()
    if not str_id in ID_LIST:
      ID_LIST.append(str_id)
ID_LIST.sort()

# ----------------------------------------
existing_id = []
for id in ID_LIST:
  result = 0
  for cmd in CMD_LIST:
    cmd = cmd % id
    cmd = cmd.split(' ')
    result = subprocess.call(cmd)
    if(0 == result):
      break
  if(0 == result):
    existing_id.append(id)

# ----------------------------------------
for result_id in existing_id:
  print result_id
