/*
    Copyright 2016-2020 Carsten Stockloew

    See the NOTICE file distributed with this work for additional
    information regarding copyright ownership

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package org.universAAL.tools.logmonitor.msgflow;

import org.universAAL.tools.logmonitor.MemberData;

/**
 * 
 * @author Carsten Stockloew
 * 
 */
public class MemberDataEx {
    public MemberData member;
    public int idxStart = 0;
    public int idxEnd = Integer.MAX_VALUE;
    public String name;
    public boolean active = true;
    public int bus = -1;

    public MemberDataEx(MemberData member, int idxStart) {
	this.member = member;
	this.idxStart = idxStart;
	if (member.label != null)
	    name = member.label;
	else
	    name = member.module + " " + member.number;

	if (MemberData.BUS_NAME_SERVICE.equals(member.busNameReadable))
	    bus = 0;
	if (MemberData.BUS_NAME_CONTEXT.equals(member.busNameReadable))
	    bus = 1;
	if (MemberData.BUS_NAME_UI.equals(member.busNameReadable))
	    bus = 2;
    }
}
