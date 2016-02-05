#!/usr/bin/env python
# coding: UTF-8

import os;
import sys;

TARGET_DIRS = ['Base', 'FW', 'Each'];

######################################################################
def change_branch(TAG):
  for dir in TARGET_DIRS:
    print "Changing the current dir to \'" + dir + "\'";
    os.chdir(dir);
    
    cmd1 = "git checkout -b " + TAG + " " + TAG;
    
    print "Executing: \'" + cmd1 + "\'";
    ret = os.system(cmd1);

    if 0 != ret:
      cmd2 = "git checkout " + TAG;
      print "  Failed(ret=" + str(ret) + ")...  Then executing: \'" + cmd2 + "\'";
      ret = os.system(cmd2);
      if 0 != ret:
        print "  Failed(ret=" + str(ret) + ")...  Then skipping dir: " + dir;
    
    print;
    os.chdir("..");

######################################################################
def execute_pull():
  for dir in TARGET_DIRS:
    print "Changing the current dir to \'" + dir + "\'";
    os.chdir(dir);
    
    cmd1 = "git checkout master";
    
    print "Executing: \'" + cmd1 + "\'";
    ret = os.system(cmd1);

    if 0 == ret:
      cmd2 = "git pull";
      print "  Succeeded...  Then executing: \'" + cmd2 + "\'";
      ret = os.system(cmd2);
      if 0 != ret:
        print "  Failed(ret=" + str(ret) + ")...  Then skipping dir: " + dir;
    
    print;
    os.chdir("..");



######################################################################


if 1 == len(sys.argv):
  print 'Usage: %s [-p] <GIT_TAG_TO_USE>' % sys.argv[0];
  print '  -p...Change the branch to master and execute \'git pull\'';
  quit();

while 1 != len(sys.argv):
  arg = sys.argv.pop(1);
  if "-p" == arg:
    execute_pull();
  else:
    change_branch(arg);
    break;
