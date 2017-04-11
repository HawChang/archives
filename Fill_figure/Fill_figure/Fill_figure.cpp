// Fill_figure.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include <cstdio>
#include <iostream>
//#include <cstring>
#include <cmath>
#include <stack>
#include <queue>
//#include <time.h>
#include <gl/glut.h>
using namespace std;
#define MAXSIZE 10000
typedef struct a{
	GLint x,y;
} point;
point points[MAXSIZE];
int num = 0;
bool finish = false;
int screenWidth = 640;
int screenHeight = 480;
int choose = 0;
int menu_id;
int X_max = 0, X_min=screenWidth;
int rgb[3];
int directions[8][2] = { { 0, 1 }, { -1, 0 }, { 1, 0 }, { 0, -1 } };
						//, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };
enum color{black,white};
void draw_pixel(GLint x, GLint y, color tar){
	switch (tar){
	case black:
		glColor3f(0.0f, 0.0f, 0.0f);//BLACK
		break;
	case white:
		glColor3f(1.0f, 1.0f, 1.0f);//WHITE
		break;
	}
	glBegin(GL_POINTS);
	glVertex2i(x, y);
	glEnd();
	glFlush();
}
bool has_drawn(GLint x,GLint y){
	glReadPixels(x, y, 1, 1, GL_RGB, GL_INT, rgb);
	if (rgb[0] != 0 && rgb[1] != 0 && rgb[2] != 0)
		return false;
	else
		return true;
}
void four_direction_seed_fill(GLint x, GLint y){
	stack<point> s;
	point p;
	p.x = x;
	p.y = y;
	draw_pixel(x,y,black);
	s.push(p);
	while (!s.empty()) {
		point temp_p = s.top();
		s.pop();
		for (int i = 0; i < 4; i++) {
			point temp;
			temp.x = temp_p.x + directions[i][0];
			temp.y = temp_p.y + directions[i][1];
			if (temp.x < 0 || temp.x > screenWidth || temp.y < 0 || temp.y > screenHeight){
				cout << "wrong! out of range" << endl;
				glClear(GL_COLOR_BUFFER_BIT);
				glutPostRedisplay();
				return;
			}
			if (!has_drawn(temp.x,temp.y)) {
				draw_pixel(temp.x,temp.y,black);
				s.push(temp);
			}
		}
	}
	cout << "finished" << endl;
}
void optimize_seed_fill(GLint x, GLint y){
	int duplicate = 0;
	stack<point> s;
	point p;
	p.x = x;
	p.y = y;
	s.push(p);
	while (!s.empty()) {
		point temp = s.top();
		s.pop();
		int left_x = temp.x, line_y = temp.y;
		if (has_drawn(left_x, line_y))
			continue;
		while (!has_drawn(left_x, line_y)) {
			draw_pixel(left_x, line_y,black);
			left_x--;
			if (left_x < 0 ){
				cout << "wrong! out of range" << endl;
				glClear(GL_COLOR_BUFFER_BIT);
				glutPostRedisplay();
				return;
			}
		}
		int right_x = temp.x + 1;
		while (!has_drawn(right_x, line_y)) {
			draw_pixel(right_x, line_y , black);
			right_x++;
			if (right_x > screenWidth){
				cout << "wrong! out of range"<< endl;
				glClear(GL_COLOR_BUFFER_BIT);
				glutPostRedisplay();
				return;
			}
		}
		bool right_most=true;
		for (int i = left_x + 1; i < right_x; i++) {
			if (!has_drawn(i, line_y + 1)) {
				if(right_most){
					point point;
					point.x = i;
					point.y = line_y + 1;
					s.push(point);
					right_most = false;
				}
			}
			else{
				duplicate++;
				right_most = true;
			}
		}
		right_most = true;
		for (int i = left_x + 1; i < right_x; i++) {
			if (!has_drawn(i, line_y - 1)) {
				if (right_most){
					point point;
					point.x = i;
					point.y = line_y - 1;
					s.push(point);
					right_most = false;
				}
			}
			else{
				duplicate++;
				right_most = true;
			}
		}
	}
	cout << "finished! duplicate:" << duplicate << endl;
}
void reverse_color(GLint x, GLint y){
	if (has_drawn(x, y))
		draw_pixel(x, y,white);
	else
		draw_pixel(x, y,black);
}
void edge_fill(GLint seed_set){
	for (int i = 0; i <= num; i++) {
		int start_x = points[i].x;
		int start_y = points[i].y;
		int end_x = points[i + 1].x;
		int end_y = points[i + 1].y;
		if (i == num) {
			start_x = points[0].x;
			start_y = points[0].y;
			end_x = points[num].x;
			end_y = points[num].y;
		}
		if (start_y == end_y) {
			continue;
		}        //横边不管
		if (start_y > end_y) {
			swap(start_x, end_x);
			swap(start_y, end_y);
		}
		double X_next = start_x;
		int dy = end_y - start_y;
		int dx = end_x - start_x;
		for (int i = start_y; i < end_y; i++) {
			if (X_next <= seed_set) {
				for (int j = X_next; j < seed_set; j++) {
					reverse_color(j, i);
				}
			}
			else {
				for (int j = seed_set; j < X_next; j++) {
					reverse_color(j, i);
				}
			}
			X_next += (double)dx / (double)dy;
		}
	}
	cout << "finished" << endl;
}
void menufunc(int option) {
	
	choose = option;
	if (option == 6){
		memset(points, 0, sizeof(points));
		finish = false;
		glutDestroyMenu(menu_id);
		num = 0;
	}
	else if(option==5){
		glClear(GL_COLOR_BUFFER_BIT);
		glutPostRedisplay();
	}
	//glutPostRedisplay();
}
void mousefunc(int button, int state, int x, int y){
	//cout << "in" << endl;
	if (button == GLUT_LEFT_BUTTON && state == GLUT_UP){
		if (finish){
			switch (choose){
			case 1:
				four_direction_seed_fill(x,screenHeight-y);
				break;
			case 2:
				optimize_seed_fill(x, screenHeight - y);
				break;
			case 3:
				edge_fill(screenWidth);
				break;
			case 4:
				edge_fill((X_max+X_min)/2);
				break;
			default:
				cout << "error!  choose is:"<<choose<<endl;
				break;
			}

		}
		else{
			points[num].x = x;
			points[num].y = screenHeight - y;
			X_max = max(X_max, x);
			X_min = min(X_min, x);
			num++;
			cout << "x:" << x << "y:" << y << endl;
			glutPostRedisplay();
		}
	}
	else if(button == GLUT_RIGHT_BUTTON && state == GLUT_DOWN){
		num--;
		finish = true;
		menu_id = glutCreateMenu(menufunc);       // 四方向种子填充
		glutAddMenuEntry("四方向种子填充", 1);         // 优化版种子填充
		glutAddMenuEntry("优化版种子填充", 2); 			//边填充
		glutAddMenuEntry("边填充", 3);		  				//栅栏填充
		glutAddMenuEntry("栅栏填充", 4);
		glutAddMenuEntry("涂色清空 保留线段", 5);
		glutAddMenuEntry("全部清空", 6);
		glutAttachMenu(GLUT_RIGHT_BUTTON);
		glutPostRedisplay();
	}
}
void display(){
	glClear(GL_COLOR_BUFFER_BIT);
	glColor3f(0.0f, 0.0f, 0.0f);//BLACK
	//glMatrixMode(GL_MODELVIEW);
	//glLoadIdentity();
	for (int i = 0; i <= num - 1; i++) {
		glBegin(GL_LINES);
		glVertex2i(points[i].x, points[i].y);
		glVertex2i(points[i + 1].x, points[i + 1].y);
		glEnd();
	}
	if (finish) {
		//drawline1(points[0][0], points[0][1], points[k - 1][0], points[k - 1][1]);
		glBegin(GL_LINES);
		glVertex2i(points[0].x, points[0].y);
		glVertex2i(points[num].x, points[num].y);
		glEnd();
			//glutSwapBuffers();
		
	}
	//glutSwapBuffers();
	glFlush();
	//glutPostRedisplay();
}
void mousemove(int x, int y){
	if (!finish){
		points[num].x = x;
		points[num].y = screenHeight - y;
		glutPostRedisplay();
	}
}
int _tmain(int argc, _TCHAR* argv[])
{
	glutInit(&argc, (char**)argv);
	glutInitDisplayMode(GLUT_DEPTH | GLUT_SINGLE | GLUT_RGBA);
	glutInitWindowPosition(250, 150);
	glutInitWindowSize(screenWidth, screenHeight);
	glutCreateWindow("填充");

	glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
	//glMatrixMode(GL_PROJECTION);
	//glLoadIdentity();
	gluOrtho2D(0, screenWidth, 0, screenHeight);
	//glMatrixMode(GL_MODELVIEW);
	//glLoadIdentity();
	//glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
	//glViewport(0, 0, 600, 600);


	glutDisplayFunc(display);
	glutMouseFunc(mousefunc);
	glutPassiveMotionFunc(mousemove);
	

	glutMainLoop();
	return 0;
}

