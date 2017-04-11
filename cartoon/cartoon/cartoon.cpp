// cartoon.cpp : �������̨Ӧ�ó������ڵ㡣
//

#include "stdafx.h"
#include <iostream>
#include <GL/glut.h>  
#include <stdio.h>  
#include <time.h>
#include <math.h>
#include <Windows.h>
#define MAX 1000
using namespace std;
int n = 100;                   //
GLfloat Pi = 3.1415926536f;
GLfloat R = 5.0f;                  //С��뾶
GLfloat center_x = 0, center_y = 0;   //С��Բ��
GLfloat Ratio=1.0f;          //���ڿ�߱���
GLfloat left_border = 0, right_border = 0, top_border = 0, bottom_border = 0;
GLfloat obstacle_left_border = 0, obstacle_right_border = 0, obstacle_top_border = 0, obstacle_bottom_border = 0;
double velocity[2] = { 0, 0 };   //С���ٶ�
//double velocity[2] = { 1, - 1 };
double gravity[2] = { 0, 0 };     //���ָ��Ϊ��������
int screenHeight = 480, screenWidth = 640;
typedef struct a{
	GLfloat position_x, position_y;
	GLfloat velocity_x, velocity_y;
	int minus;
}obstacle;
obstacle obstacles[MAX];
int obstacle_num = 0;
int obstacle_R = 2;
int score = 0;
int life = 3;
void selectFont(int size, int charset, const char* face)  {
	HFONT hFont = CreateFontA(size,    //ָ���ָ�   
		0,     //ָ���ֿ�  
		0,      //ָ���Ƕ�(1/10��) 
		45,          //ָ���ַ��Ļ�����x��ĽǶ�(1/10��)  
		FW_BOLD,   //ָ�����������(FW_BOLD=700)  
		0,     //ָ���Ƿ�б��  
		0,     //ָ���Ƿ����»���  
		0,        //ָ���Ƿ���StrikeOut����  
		charset,   
		OUT_DEFAULT_PRECIS, //ָ���������  
		CLIP_DEFAULT_PRECIS,//ָ���ü�����   
		DEFAULT_QUALITY, //ָ���������   
		DEFAULT_PITCH | FF_SWISS, //ָ������Ķ�λ�����    
		face);  
		HFONT hOldFont = (HFONT)SelectObject(wglGetCurrentDC(), hFont); 
		DeleteObject(hOldFont); 
}
void drawCNString(const char* str) {
	int len, i;
	wchar_t* wstring;
	HDC hDC = wglGetCurrentDC();
	GLuint list = glGenLists(1);
	// �����ַ��ĸ���
	// �����˫�ֽ��ַ��ģ����������ַ����������ֽڲ���һ���ַ�
	// ����һ���ֽ���һ���ַ�
	len = 0;
	for (i = 0; str[i] != '\0'; ++i)
	{
		if (IsDBCSLeadByte(str[i]))
			++i;
		++len;
	}
	// ������ַ�ת��Ϊ���ַ�
	wstring = (wchar_t*)malloc((len + 1) * sizeof(wchar_t));
	MultiByteToWideChar(CP_ACP, MB_PRECOMPOSED, str, -1, wstring, len);
	wstring[len] = L'\0';
	// �������ַ�
	for (i = 0; i<len; ++i)
	{
		wglUseFontBitmapsW(hDC, wstring[i], 1, list);
		glCallList(list);
	}

	// ����������ʱ��Դ
	free(wstring);
	glDeleteLists(list, 1);
}
void display_obstacle(){
	// OpenGL����õ�ǰ�Ļ�ͼ��ɫ����һ��������
	//glRectf(-25.0f, 25.0f, 25.0f, -25.0f);
	for (int j = 0; j < obstacle_num; j++){
		if (obstacles[j].minus == 1) glColor3f(0.22f, 0.68f, 0.22f);
		else if (obstacles[j].minus==2) glColor3f(0.0f, 0.0f, 1.0f);
		else glColor3f(1.0f, 0.0f, 0.0f);
		glBegin(GL_LINE_LOOP);      //�����߶� �ұպ�
		for (int i = 0; i<n; ++i)
			glVertex2f(obstacles[j].position_x + obstacle_R*cos(2 * Pi / n*i), obstacles[j].position_y + obstacle_R*sin(2 * Pi / n*i));
		glEnd();
	}
	glFlush();
}
// ���Ƴ���
void RenderScene()
{
	// OpenGL��������ɫ������
	glClearColor(0.75, 0.75, 0.75, 0.0);
	glClear(GL_COLOR_BUFFER_BIT);
	glColor3f(0.0f, 0.0f, 0.0f);
	// OpenGL����õ�ǰ�Ļ�ͼ��ɫ����һ��������
	//glRectf(-25.0f, 25.0f, 25.0f, -25.0f);
	glBegin(GL_LINE_LOOP);      //�����߶� �ұպ�
	for (int i = 0; i<n; ++i)
		glVertex2f(center_x+R*cos(2 * Pi / n*i), center_y+R*sin(2 * Pi / n*i));
	glEnd();
	char buffer[20];
	_itoa_s(score, buffer, 10);
	char one[100] = "����:";
	strcat_s(one, buffer);
	//string	one = "������" + score;
	//char* p = const_cast<char*>(one.c_str());
	//one.copy(p,sizeof(one),0);
	//*(p + sizeof(one)) = '\0';
	display_obstacle();
	glColor3f(0.68f, 0.22f, 0.22f);
	glRasterPos2f(left_border-R,top_border+R-10);
	drawCNString(one);
	_itoa_s(life, buffer, 10);
	char two[100] = "������";
	strcat_s(two, buffer);
	glRasterPos2f(left_border - R, top_border + R - 20);
	drawCNString(two);
	_itoa_s(R, buffer, 10);
	char three[100] = "��ǰ�뾶:";
	strcat_s(three, buffer);
	glRasterPos2f(left_border - R, top_border + R - 30);
	drawCNString(three);
	glFlush();
}
void SetupRC()
{
	// ��������������ڵ���ɫ
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
}

// �����ڴ�С�ı�ʱ��GLUT���������
void ChangeSize(GLsizei w, GLsizei h)
{
	//cout << "width: " << w << " height:" << h << endl;
	// ��ֹ��0����
	if (0 == h){
		h = 1;
	}
	// �����ӿ�Ϊ���ڵĴ�С
	glViewport(0, 0, w, h);
	// ѡ��ͶӰ���󣬲���������ϵͳ
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	screenHeight = h;
	screenWidth = w;
	// ���㴰�ڵ��ݺ�ȣ����رȣ�
	Ratio = (GLfloat)w / (GLfloat)h;
	if (w <= h) {// �� < ��
		glOrtho(-100.0, 100.0, -100 / Ratio, 100 / Ratio, 1.0, -1.0);
		left_border = -100+R;
		right_border = 100-R;
		top_border = 100 / Ratio - R;
		bottom_border = -100 / Ratio + R;     //���崰�ڱ߽�
		obstacle_left_border = -100 + obstacle_R;
		obstacle_right_border = 100 - obstacle_R;
		obstacle_top_border = 100 / Ratio - obstacle_R;
		obstacle_bottom_border = -100 / Ratio + obstacle_R;     //���崰�ڱ߽�
	}
	else {// �� > ��
		glOrtho(-100.0 * Ratio, 100.0 *Ratio, -100.0, 100.0, 1.0, -1.0);
		left_border = -100*Ratio+R;
		right_border = 100*Ratio-R;
		top_border = 100-R	;
		bottom_border = -100+R;         //���崰�ڱ߽�
		obstacle_left_border = -100*Ratio + obstacle_R;
		obstacle_right_border = 100*Ratio - obstacle_R;
		obstacle_top_border = 100 - obstacle_R;
		obstacle_bottom_border = -100 + obstacle_R;     //���崰�ڱ߽�
	}
	// ѡ��ģ����ͼ���󣬲���������ϵͳ
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
}
//void move1(){
	//cout << center_x << center_y << endl;
//	if (center_y <= bottom_border || center_y >= top_border){
//		velocity[1] = -velocity[1];
//	}
//	if (center_x <= left_border||center_x >= right_border){
//		velocity[0] = -velocity[0];
//	}
//	velocity[1] -= 1;
//	center_x += velocity[0];
//	center_y += velocity[1];
//	if (center_y > top_border) center_y = top_border;
//	if (center_y < bottom_border) center_y = bottom_border;
//	if (center_x < left_border) center_x = left_border;
//	if (center_x > right_border) center_x = right_border;    //�߽��ж�  ����ע�͵�
//	glutPostRedisplay();
//	Sleep(30);
//}
void velocity_set(){
	if (center_y <= bottom_border){
		velocity[1] = -velocity[1];
	}
	if (center_y >= top_border){
		velocity[1] = -velocity[1];
	}
	if (center_x <= left_border){
		velocity[0] = -velocity[0];
	}
	if (center_x >= right_border){
		velocity[0] = -velocity[0];    //�߽���ײ ��ײʱ �ٶȷ��� ����ע�͵�  ��������������������ײ
	}
	double d = sqrt(pow((gravity[0] - center_x), 2) + pow((gravity[1] - center_y), 2));
	if (d == 0) {
		velocity[0] = 0;
		velocity[1] = 0;
	}
	else{
		double cos = (gravity[1] - center_y) / d;
		double sin = (gravity[0] - center_x) / d;
		velocity[0] += sin;
		velocity[1] += cos;                      //sin cos�Ǽ��ٶ�1 ˮƽ����ʹ�ֱ����ķ���          
	}
	velocity[0] *= 0.98;
	velocity[1] *= 0.98;  //�ٶ�������
	if (pow(velocity[0], 2) + pow(velocity[1], 2) < 1 && d<2){ //�ٶȹ��� �������Բ������Ͻ�ʱ ֹͣ
		velocity[0] = 0;
		velocity[1] = 0;
		center_x = gravity[0];
		center_y = gravity[1];            //ͣ������ ����ע�͵�
	}
}
void generate_obstacles(){
	if (obstacle_num < 7){
		int position_x, position_y;
		GLfloat velocity_x, velocity_y;
		int i = rand() % 4;
		cout << "case:" << i << endl;
		switch(i){
		case 0:
			position_x = obstacle_left_border;
			position_y = rand() % 201 - 100;
			velocity_x = (double)(rand() % 100) / 150;
			velocity_y = (double)(rand() % 201 - 100) / 150;
			break;
		case 1:
			position_x = rand() % 201 - 100;
			position_y = obstacle_bottom_border;
			velocity_y = (double)(rand() % 100) / 150;
			velocity_x = (double)(rand() % 201 - 100) / 150;
			break;
		case 2:
			position_x = obstacle_right_border;
			position_y = rand() % 201 - 100;
			velocity_x = -(double)(rand() % 100) / 150;
			velocity_y = (double)(rand() % 201 - 100) / 150;
			break;
		case 3:
			position_x = rand() % 201 - 100;
			position_y = obstacle_top_border;
			velocity_y = -(double)(rand() % 100) / 150;
			velocity_x = (double)(rand() % 201 - 100) / 150;
			break;
		}
		cout << "obstacle num:" << obstacle_num << endl;
		cout << "position_x:" << position_x << endl;
		cout << "position_y:" << position_y << endl;
		cout << "velocity_x:" << velocity_x << endl;
		cout << "velocity_y:" << velocity_y << endl;
		obstacles[obstacle_num].position_x = position_x;
		obstacles[obstacle_num].position_y = position_y;
		obstacles[obstacle_num].velocity_x = velocity_x;
		obstacles[obstacle_num].velocity_y = velocity_y;
		int type = rand() % 100;
		if (type> 25){
			obstacles[obstacle_num].minus = 1;
		}
		else if(type>5){
			obstacles[obstacle_num].minus = 2;
		}
		else{
			obstacles[obstacle_num].minus = 3;
		}
		obstacle_num++;
	}
}
void remove_obstacle(int i){
	for (int j = i; j < obstacle_num - 1; j++){
		obstacles[j].position_x = obstacles[j + 1].position_x;
		obstacles[j].position_y = obstacles[j + 1].position_y;
		obstacles[j].velocity_x = obstacles[j + 1].velocity_x;
		obstacles[j].velocity_y = obstacles[j + 1].velocity_y;
		obstacles[j].minus = obstacles[j + 1].minus;
	}
	obstacle_num--;
}
void obstacle_move(){
	//cout << "obstacle num:" << obstacle_num << endl;
	for (int i = obstacle_num-1; i >=0; i--){
		//cout << i << endl;
		obstacles[i].position_x += obstacles[i].velocity_x;
		obstacles[i].position_y += obstacles[i].velocity_y;
		if (obstacles[i].position_y > obstacle_top_border || obstacles[i].position_y < obstacle_bottom_border || obstacles[i].position_x < obstacle_left_border || obstacles[i].position_x > obstacle_right_border) {
			remove_obstacle(i);
		}
	}
	generate_obstacles();
}
void check_eat(){
	for (int i = obstacle_num-1; i >= 0; i--){
		if (pow((obstacles[i].position_x - center_x), 2) + pow((obstacles[i].position_y - center_y), 2) < pow((R + obstacle_R), 2)){
			if (obstacles[i].minus == 1) {
				score++;
				R += 1;
			}
			else if (obstacles[i].minus == 2) {
				score++;
				if (R>=4) R -= 2;
			}
			else if (obstacles[i].minus == 3) {
				life--;
				if (life <= 0){
					exit(0);
				}
			}
			ChangeSize(screenWidth, screenHeight);
			remove_obstacle(i);
		}
	}
}
void move(){
	//cout <<"center_x"<<center_x<<"center_y"<<center_y << endl;
	velocity_set();
	obstacle_move();
	check_eat();
	center_x += velocity[0];
	center_y += velocity[1]; 
	if (center_y > top_border) center_y = top_border;
	if (center_y < bottom_border) center_y = bottom_border;
	if (center_x < left_border) center_x = left_border;
	if (center_x > right_border) center_x = right_border;    //�߽��ж�  ����ע�͵�
	//if (center_y > top_border || center_y < bottom_border || center_x<left_border || center_x>right_border){
	//	while (true);
	//}
	glutPostRedisplay();
	Sleep(30);
}
void mouseMove(int x, int y){
	if (Ratio<=1) {// �� < ��
		gravity[0] = (x-screenWidth/2)*200/screenWidth;
		gravity[1] = (screenHeight/2 -y)*200/(screenHeight*Ratio);   //�������任
	}
	else {// �� > ��
		gravity[0] = (x - screenWidth/2) * 200 * Ratio / screenWidth;
		gravity[1] = (screenHeight/2 - y ) * 200 / screenHeight;
	}
}
int main(int argc, char *argv[])
{
	// ���������в���������GLUT��������г�ʼ��
	glutInit(&argc, argv);
	// ���ô�������ʱ����ʾģʽ������������RGBA��ɫģʽ��
	glutInitDisplayMode(GLUT_SINGLE | GLUT_RGBA);
	glutInitWindowPosition(100, 100);
	glutInitWindowSize(screenWidth, screenHeight);
	// ��������
	glutCreateWindow("С��");
	glLineWidth(3);
	srand((unsigned)time(0));
	selectFont(16, GB2312_CHARSET, "��Բ");
	// ������ʾ�ص�����
	glutDisplayFunc(RenderScene);
	glutIdleFunc(move);
	memset(obstacles, 0, sizeof(obstacles));
	// ���õ����ڵĴ�С�����仯ʱ�Ļص�����
	glutReshapeFunc(ChangeSize);
	glutPassiveMotionFunc(mouseMove);
	// ������Ⱦ״̬
	SetupRC();
	glutMainLoop();

	return 0;
}
