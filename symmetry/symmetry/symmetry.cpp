// symmetry.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include "stdafx.h"
#include <cstdio>
#include <iostream>
#include <cstring>
#include <cmath>
#include <gl/glut.h>
using namespace std;
#define MAXSIZE 10000
typedef struct a{
	GLint x, y;
	GLint minus_x, minus_y;
} point;
point points[MAXSIZE];
int num = 0;
bool finish = false;
int screenWidth = 640;
int screenHeight = 480;
void display(){
	glClear(GL_COLOR_BUFFER_BIT);
	glColor3f(0.0f, 0.0f, 0.0f);//BLACK
	if (finish){
		for (int i = 0; i < num; i++) {
			//int minus_x = screenWidth - points[i].x;
			//int minus_y = screenHeight - points[i].y;
			glBegin(GL_LINES);
			glVertex2i(points[i].x, points[i].y);
			glVertex2i(points[i + 1].x, points[i + 1].y);
			glVertex2i(points[i].minus_x, points[i].y);
			glVertex2i(points[i + 1].minus_x, points[i + 1].y);
			glVertex2i(points[i].x, points[i].minus_y);
			glVertex2i(points[i + 1].x, points[i + 1].minus_y);
			glVertex2i(points[i].minus_x, points[i].minus_y);
			glVertex2i(points[i + 1].minus_x, points[i + 1].minus_y);
			glEnd();
		}
		glBegin(GL_LINES);
		glVertex2i(points[0].x, points[0].y);
		glVertex2i(points[num].x, points[num].y);
		glVertex2i(points[0].minus_x, points[0].y);
		glVertex2i(points[num].minus_x, points[num].y);
		glVertex2i(points[0].x, points[0].minus_y);
		glVertex2i(points[num].x, points[num].minus_y);
		glVertex2i(points[0].minus_x, points[0].minus_y);
		glVertex2i(points[num].minus_x, points[num].minus_y);
		
		glEnd();
	}
	else{
		for (int i = 0; i < num; i++) {
			glBegin(GL_LINES);
			glVertex2i(points[i].x, points[i].y);
			glVertex2i(points[i + 1].x, points[i + 1].y);
			glEnd();
		}
	}
	glBegin(GL_LINES);
	glVertex2i(screenWidth / 2, 0);
	glVertex2i(screenWidth / 2, screenHeight);
	glVertex2i(0, screenHeight / 2);
	glVertex2i(screenWidth, screenHeight / 2);
	glEnd();
	glutSwapBuffers();
}
void mouse(int button, int state, int x, int y)
{
	if (button == GLUT_LEFT_BUTTON && state == GLUT_UP){
		if (!finish){
			points[num].x = x;
			points[num].y = screenHeight - y;
			points[num].minus_x = screenWidth - x;
			points[num].minus_y = y;
			num++;
			cout << "x:" << x << "y:" << y << endl;
			glutPostRedisplay();
		}
	}
	else if (button == GLUT_RIGHT_BUTTON && state == GLUT_DOWN){
		if (finish){
			
		}
		else{
			num--;
			finish = true;
			glutPostRedisplay();
		}
	}

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

	memset(points, 0, sizeof(points));
	glutDisplayFunc(display);
	glutMouseFunc(mouse);
	glutPassiveMotionFunc(mousemove);
	glutMainLoop();
	return 0;
}

