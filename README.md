# Examination Timetabling using CSP Solver

CONTENTS OF THIS FILE
---------------------
 * Introduction
 * Usage
 * Overview

INTRODUCTION
------------
When looking at **examination schedules**, and how packed they can be, one starts to think
if it is fair enough to decide more than 50% of the semester’s grade in a matter of hours. In some
cases, students had to take more than one exam in a single day, sometimes with not a single
minute between the two. Typically, a university has over 10,000 students taking over 500
courses (total of all the courses), and this huge number of variables makes it near impossible to
come up with a perfect exam schedule in an exam period that is less than 2 weeks long.

The method that was used to solve this problem was the **Constraint Satisfaction Problem
(CSP)**, which relied on hard “obligatory” constraints and soft “preferred” constraints. CSP
depends on considering each exam as a variable and the timeslots as values that can be assigned
to these variables. The goal of the project is to come up with the best method to assign these
values (timeslots) to the variables in order to come up with the best schedule, without leaving
an exam empty valued. An example of a CSP problem, the graph coloring problem, can be seen below.

<p align="center">
  <img width="460" height="300" src="https://www.globalsoftwaresupport.com/wp-content/uploads/2018/03/ezgif.com-video-to-gif-34.gif">
</p>

This work focuses on solving a **real-world examination-timetabling problem at Birzeit
University (BZU)**. Since Birzeit University has a large number of students (approximately
13000) students, it must have an efficient program or algorithm to create schedules for all
students. The main goal of this work is to make the timetabling process efficient, automated and
takes into consideration the overall situation for students taking exams. Moreover, most
timetabling problems focus only on the hard constraints, for example, in BZU not having three
exams in the same day, this work will take into account other constraints “soft constraints”.
These constraints make the timetabling process more complicated but in return, it creates
schedules that are more efficient for students and gives them the needed time.

INSTALLATION
------------
This program relies on a database of confidential student info from Birzeit University, which cannot be shared. For this reason, the code is available, but a sample database does not exist as of this moment. Please check Driver/src to see the project code.

Overview
------------
A complete overview (project report, along with slides) has been added to the **project-overview** directory.

