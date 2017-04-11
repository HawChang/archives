// picture_move.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include <cstdio>
#include <iostream>
#include <cstring>
#include <cmath>
#include <gl/glut.h>
using namespace std;
#define MAXSIZE 10000
#define VELOCITY 1.2
enum type{ mid_line, bre_line, mid_circle, bre_circle };
typedef struct a{
	GLint origin_x, origin_y;
	GLint display_x, display_y;
} draw;
draw draws[MAXSIZE];
int num = 0;
bool finish =false;
bool drag = false,rotate_ready=false;
int screenWidth = 640;
int screenHeight = 480;
int choose = 0;
int origin_x = 0, origin_y = 0;
double origin_cos_theta = 0, origin_sin_theta = 0;
//int rotate_start_x = 0, rotate_start_y = 0;
void display(){
	glClear(GL_COLOR_BUFFER_BIT);
	glColor3f(0.0f, 0.0f, 0.0f);//BLACK
	//glMatrixMode(GL_MODELVIEW);
	//glLoadIdentity();
	for (int i = 0; i < num; i++) {
		glBegin(GL_LINES);
		glVertex2i(draws[i].display_x, draws[i].display_y);
		glVertex2i(draws[i + 1].display_x, draws[i + 1].display_y);
		glEnd();
	}
	if (finish) {
		//drawline1(points[0][0], points[0][1], points[k - 1][0], points[k - 1][1]);
		glBegin(GL_LINES);
		glVertex2i(draws[0].display_x, draws[0].display_y);
		glVertex2i(draws[num].display_x, draws[num].display_y);
		glEnd();
		//glutSwapBuffers();

	}
	glutSwapBuffers();
}
void mouse(int button, int state, int x, int y)
{
	if (button == GLUT_LEFT_BUTTON && state == GLUT_UP){
		if (!finish){
			draws[num].origin_x = x;
			draws[num].display_x = x;
			draws[num].origin_y = screenHeight - y;
			draws[num].display_y = screenHeight - y;
			num++;
			cout << "x:" << x << "y:" << y << endl;
			glutPostRedisplay();
		}else{
			//cout << "record :" << "x:" << x << "y:" << y << endl;
			//origin_y = screenHeight - y;
			//origin_x = x;
			for (int i = 0; i <= num; i++){
				draws[i].origin_x = draws[i].display_x;
				draws[i].origin_y = draws[i].display_y;
			}
			drag = false;
			cout << "drag finish" << endl;
		}
	}
	else if (button == GLUT_LEFT_BUTTON && state == GLUT_DOWN && finish){
		cout << "record :" << "x:" << x << "y:" << y << endl;
		origin_y = screenHeight - y;
		origin_x = x;
		cout << "ready to drag"<<endl;
		drag = true;
	}
	else if (button == GLUT_RIGHT_BUTTON && state == GLUT_DOWN){
		if (finish){
			rotate_ready = true;
			//rotate_start_x = x;
			//rotate_start_y = screenHeight - y;
			int R= pow((x - origin_x), 2) + pow((screenHeight - y - origin_y), 2);
			origin_cos_theta = (x - origin_x) / sqrt(R);
			origin_sin_theta = (screenHeight - y - origin_y) / sqrt(R);
			cout << "ready to rotate" << endl;
		}
		else{
			num--;
			finish = true;
			glutPostRedisplay();
		}
	}
	else if (button == GLUT_RIGHT_BUTTON && state == GLUT_UP){
		if (rotate_ready){
			for (int i = 0; i <= num; i++){
				draws[i].origin_x = draws[i].display_x;
				draws[i].origin_y = draws[i].display_y;
			}
			rotate_ready = false;
			cout << "rotate finish" << endl;
		}
	}
	else if (button == GLUT_WHEEL_UP && state == GLUT_UP){
		cout << "wheel up!"<<endl;
		for (int i = 0; i <= num; i++){
			draws[i].display_x = (draws[i].display_x - x)*VELOCITY + x;
			draws[i].display_y = (draws[i].display_y - (screenHeight-y))*VELOCITY + (screenHeight-y);
			draws[i].origin_x = draws[i].display_x;
			draws[i].origin_y = draws[i].display_y;
		}
		glutPostRedisplay();
	}
	else if (button == GLUT_WHEEL_DOWN && state == GLUT_UP){
		cout << "wheel down!" << endl;
		for (int i = 0; i <= num; i++){
			draws[i].display_x = (draws[i].display_x - x)/VELOCITY + x;
			draws[i].display_y = (draws[i].display_y - (screenHeight - y))/VELOCITY + (screenHeight - y);
			draws[i].origin_x = draws[i].display_x;
			draws[i].origin_y = draws[i].display_y;
		}
		glutPostRedisplay();
	}
}
void mousemove(int x, int y){
	if (!finish){
		draws[num].display_x = x;
		draws[num].display_y = screenHeight - y;
		glutPostRedisplay();
	}
}
void mousedrag(int x, int y){
	if (drag){
		int dx = x - origin_x, dy = screenHeight - y - origin_y;
		for (int i = 0; i <= num; i++){
			draws[i].display_x = draws[i].origin_x+dx;
			draws[i].display_y = draws[i].origin_y + dy;
		}
		glutPostRedisplay();
	}
	else if(rotate_ready){
		//cout << "in" << endl;
		int R = pow((x - origin_x), 2) + pow((screenHeight-y - origin_y), 2);
		double cos_temp = (x-origin_x)/sqrt(R);
		double sin_temp = (screenHeight-y - origin_y) / sqrt(R);
		double cos_theta = cos_temp*origin_cos_theta + origin_sin_theta * sin_temp;
		double sin_theta = sin_temp*origin_cos_theta - origin_sin_theta*cos_temp;
		//cout <<"cos:"<<cos_theta<<" , sin:"<<sin_theta<< endl;
		for (int i = 0; i <= num; i++){
				draws[i].display_x = (draws[i].origin_x - origin_x)*cos_theta - (draws[i].origin_y - origin_y)*sin_theta + origin_x;
				draws[i].display_y = (draws[i].origin_x - origin_x)*sin_theta + (draws[i].origin_y - origin_y)*cos_theta + origin_y;
		}
		glutPostRedisplay();
	}
}
int _tmain(int argc, _TCHAR* argv[])
{
	glutInit(&argc, (char**)argv);
	glutInitDisplayMode(GLUT_DEPTH | GLUT_DOUBLE | GLUT_RGBA);//深度缓存 双缓存 RGBA模式窗口
	glutInitWindowPosition(100, 100);
	glutInitWindowSize(screenWidth, screenHeight);
	glutCreateWindow("实验1");

	//glMatrixMode(GL_PROJECTION);
	//glLoadIdentity();
	gluOrtho2D(0, screenWidth, 0, screenHeight);
	//glMatrixMode(GL_MODELVIEW);
	glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
	//glViewport(0,0,600,600);

	memset(draws, 0, sizeof(draws));
	glutDisplayFunc(display);
	glutMouseFunc(mouse);
	glutPassiveMotionFunc(mousemove);
	glutMotionFunc(mousedrag);
	glutMainLoop();
	return 0;
}

