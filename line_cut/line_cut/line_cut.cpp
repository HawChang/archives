// line_cut.cpp : 定义控制台应用程序的入口点。
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
	double x, y;
} point;
typedef struct c{
	int start_x, end_x;
	double k, b;
}polynomia;
typedef struct d{
	double start_x, start_y, end_x, end_y;
} line;
point vertexs[MAXSIZE];
polynomia polynomias[MAXSIZE];
line lines[MAXSIZE];
int vertex_num = 0, line_num = 0;
bool finish = false,is_pair=true;
int screenWidth = 640;
int screenHeight = 480;
void display(){
	glClear(GL_COLOR_BUFFER_BIT);
	glColor3f(0.0f, 0.0f, 0.0f);//BLACK
	if (finish){
		for (int i = 0; i < vertex_num; i++) {
			glBegin(GL_LINES);
			glVertex2i(vertexs[i].x, vertexs[i].y);
			glVertex2i(vertexs[i + 1].x, vertexs[i + 1].y);
			glEnd();
		}
		glBegin(GL_LINES);
		glVertex2i(vertexs[0].x, vertexs[0].y);
		glVertex2i(vertexs[vertex_num].x, vertexs[vertex_num].y);
		glEnd();
		glColor3f(0.0f, 0.0f, 1.0f);
		for (int i = 0; i <= line_num; i++){
			glBegin(GL_LINES);
			glVertex2i(lines[i].start_x, lines[i].start_y);
			glVertex2i(lines[i].end_x, lines[i].end_y);
			glEnd();
		}
	}
	else{
		for (int i = 0; i < vertex_num; i++) {
			glBegin(GL_LINES);
			glVertex2i(vertexs[i].x, vertexs[i].y);
			glVertex2i(vertexs[i + 1].x, vertexs[i + 1].y);
			glEnd();
		}
	}
	glutSwapBuffers();
}
bool check_point(double x, double y){
	//cout << "x :" << x << "y:" << y << endl;
	int count = 0;
	double temp;
	for (int i = 0; i <= vertex_num; i++) {
		temp = (y - polynomias[i].b) / polynomias[i].k;
		//cout << "caculate x:"<<temp<< endl;
		if ( temp>= __max(x,polynomias[i].start_x)&&temp<=polynomias[i].end_x){
			count++;
			//cout << "in"<< endl;
		}
		else{
			//cout << "not in" << endl;
		}
	}
	if (count % 2 == 0){
		//cout << "outside! count=" <<count<< endl;
		return false;
	}
	else {
		//cout << "inside!! count=" <<count<< endl;
		if (y - (int)y == 0){
			if (check_point(x, y + 0.5) || check_point(x, y - 0.5)){
				
				return true;
			} else{
				cout << y << endl;
				return false;
			}
		}
		return true;
	}
	glutPostRedisplay();
}
bool check_line(line l){
	double x1 = l.start_x;
	double x2 = l.end_x;
	double y1 = l.start_y;
	double y2 = l.end_y;
	if (x2 == x1){
		//cout << "x2==x1"<< endl;
		if (y1 > y2){
			swap(y1, y2);
		}
		//cout << y1 << "   ,  " << y2 << endl;
		for (int i = y1; i <= y2; i++){
			cout << i<< endl;
			if(check_point(x1, i)) return true;
		}
		return false;
	}
	else if (y2==y1){
		//cout << "y2==y1" << endl;
		if (x1 > x2){
			swap(x1, x2);
		}
		for (int i = x1; i <= x2; i++){
			if (check_point(i, y1)) return true;
		}
		return false;
	}
	else {
		if (abs((y2 - y1) / (x2 - x1)) > 1){
			//cout << x1<<"...." << endl;
			if (y1 > y2){
				//cout <<x1<<","<<x2 << endl;
				swap(x1, x2);
				//cout << x1 << "," << x2 << endl;
				swap(y1, y2);
			}
			double k = (x2 - x1) / (y2 - y1);
			//cout << "k=" << (y2-y1)/(x2-x1) << endl;
			for (int i = y1; i <= y2; i++){
				//cout <<"x :"<< x1 + (i - y1)*k <<"y:"<<i<< endl;
				if (check_point(x1 + (i - y1)*k, i)) return true;
			}
			return false;
		}
		else{
			if (x1 > x2){
				swap(x1, x2);
				swap(y1, y2);
			}
			double k = (y2 - y1) / (x2 - x1);
			for (int i = x1; i <= x2; i++){
				//cout << "y :" << y1 + (i - x1)*k <<"x:"<<i<< endl;
				if (check_point(i, y1 + (i - x1)*k)) return true;
			}
			return false;
		}
	}
}
point nearest(double start_x, double end_x, double start_y, double end_y){
	point result;
	line temp;
	temp.start_x = start_x;
	temp.end_x = end_x;
	temp.start_y = start_y;
	temp.end_y = end_y;
	if (!check_point(start_x, start_y)){
		while (pow(temp.start_x - temp.end_x, 2) + pow(temp.start_y - temp.end_y, 2)>1){
			//cout << pow(temp.start_x - temp.end_x, 2) + pow(temp.start_y - temp.end_y, 2) << endl;
			temp.end_y = (temp.start_y + temp.end_y) / 2;
			temp.end_x = (temp.start_x + temp.end_x) / 2;
			if (check_line(temp)){
				continue;
			}
			else{
				temp.end_x = temp.end_x * 2 - temp.start_x;
				temp.end_y = temp.end_y * 2 - temp.start_y;
				temp.start_x = (temp.start_x + temp.end_x) / 2;
				temp.start_y = (temp.start_y + temp.end_y) / 2;
			}
		}
	}
	result.x = temp.start_x;	
	result.y = temp.start_y;
	return result;
}

bool cut(line &l){
	//int start_x = l.start_x;
	//int end_x = l.end_x;
	//int start_y = l.start_y;
	//int end_y = l.end_y;
	if (check_line(l)){
		point result_start, result_end;
		result_start = nearest(l.start_x,l.end_x,l.start_y,l.end_y);
		result_end = nearest(l.end_x, l.start_x, l.end_y, l.start_y);
		l.start_x = result_start.x;
		l.start_y = result_start.y;
		l.end_x = result_end.x;
		l.end_y = result_end.y;
		return true;
	}
	else{
		//cout << "cut all"<<endl;

		return false;
	}
	glutPostRedisplay();

}
void menufunc(int option) {
	if (option == 1){
		for (int i = 0; i < line_num; i++){
			if (!cut(lines[i])){
				for (int j = i; j < line_num; j++){
					lines[j] = lines[j + 1];
				}
				line_num--;
				i--;
				//lines[i].start_x = 0;
				//lines[i].start_y = 0;
				//lines[i].end_x = 0;
				//lines[i].end_y = 0;
			}
			//if (check_line(lines[i])){
			//	cout <<"in" << endl;
			//}
			//else{
			//	cout << "not in" << endl;
			//}
		}
	}
	glutPostRedisplay();
}
void mouse(int button, int state, int x, int y)
{
	if (button == GLUT_LEFT_BUTTON && state == GLUT_UP){
		if (!finish){
			vertexs[vertex_num].x = x;
			vertexs[vertex_num].y = screenHeight - y;
			vertex_num++;
			//cout << "x:" << x << "y:" << y << endl;
			glutPostRedisplay();
		}
		else{
			if (is_pair){
				lines[line_num].start_x = x;
				lines[line_num].start_y = screenHeight - y;
				is_pair = false;
				glutPostRedisplay();
			}
			else{
				lines[line_num].end_x = x;
				lines[line_num].end_y = screenHeight - y;
				is_pair = true;
				line_num++;
				glutPostRedisplay();
			}
			//cout << "x:" << x << "y:" << y << endl;
			//if (check_point(x, y)){
			//	
			//	cout << "in"<< endl;
			//}
			//else{
			//	cout << "not in" << endl;
			//}
		}
	}
	else if (button == GLUT_RIGHT_BUTTON && state == GLUT_DOWN){
		if (finish){
			//int menu_id = glutCreateMenu(menufunc);     
		//glutAddMenuEntry("剪裁", 1);       
		//	glutAttachMenu(GLUT_RIGHT_BUTTON);
		}
		else{
			vertex_num--;
			finish = true;
			for (int i = 0; i < vertex_num; i++) {
				polynomias[i].k = ((double)(vertexs[i].y - vertexs[i + 1].y)) / (vertexs[i].x - vertexs[i + 1].x);
				polynomias[i].b = ((double)(vertexs[i].x*vertexs[i + 1].y - vertexs[i + 1].x*vertexs[i].y)) / (vertexs[i].x - vertexs[i + 1].x);
				polynomias[i].start_x = __min(vertexs[i].x,vertexs[i+1].x);
				polynomias[i].end_x = __max(vertexs[i].x, vertexs[i + 1].x);
				//cout << "k:" << polynomias[i].k << " b:" << polynomias[i].b << endl;
			
			}
			polynomias[vertex_num].k = ((double)(vertexs[0].y - vertexs[vertex_num].y)) / (vertexs[0].x - vertexs[vertex_num].x);
			polynomias[vertex_num].b = ((double)(vertexs[0].x*vertexs[vertex_num].y - vertexs[vertex_num].x*vertexs[0].y)) / (vertexs[0].x - vertexs[vertex_num].x);
			polynomias[vertex_num].start_x = __min(vertexs[0].x, vertexs[vertex_num].x);
			polynomias[vertex_num].end_x = __max(vertexs[0].x, vertexs[vertex_num].x);
			//cout << "k:" << polynomias[vertex_num].k << " b:" << polynomias[vertex_num].b << endl;
			//cout << "vertex_num="<<vertex_num<< endl;
			glutPostRedisplay();
			int menu_id = glutCreateMenu(menufunc);
			glutAddMenuEntry("剪裁", 1);
			glutAttachMenu(GLUT_RIGHT_BUTTON);
		}
	}

}
void mousemove(int x, int y){
	if (finish){
		if (!is_pair){
			lines[line_num].end_x = x;
			lines[line_num].end_y = screenHeight - y;
			glutPostRedisplay();
		}
		
	}
	else{
		vertexs[vertex_num].x = x;
		vertexs[vertex_num].y = screenHeight - y;
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

	memset(vertexs, 0, sizeof(vertexs));
	glutDisplayFunc(display);
	glutMouseFunc(mouse);
	glutPassiveMotionFunc(mousemove);
	glutMainLoop();
	return 0;
}

