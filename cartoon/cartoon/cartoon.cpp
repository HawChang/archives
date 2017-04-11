// cartoon.cpp : 定义控制台应用程序的入口点。
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
GLfloat R = 5.0f;                  //小球半径
GLfloat center_x = 0, center_y = 0;   //小球圆心
GLfloat Ratio=1.0f;          //窗口宽高比例
GLfloat left_border = 0, right_border = 0, top_border = 0, bottom_border = 0;
GLfloat obstacle_left_border = 0, obstacle_right_border = 0, obstacle_top_border = 0, obstacle_bottom_border = 0;
double velocity[2] = { 0, 0 };   //小球速度
//double velocity[2] = { 1, - 1 };
double gravity[2] = { 0, 0 };     //鼠标指向为重力方向
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
	HFONT hFont = CreateFontA(size,    //指定字高   
		0,     //指定字宽  
		0,      //指定角度(1/10度) 
		45,          //指定字符的基线与x轴的角度(1/10度)  
		FW_BOLD,   //指定字体的重量(FW_BOLD=700)  
		0,     //指定是否斜体  
		0,     //指定是否有下划线  
		0,        //指定是否是StrikeOut字体  
		charset,   
		OUT_DEFAULT_PRECIS, //指定输出精度  
		CLIP_DEFAULT_PRECIS,//指定裁剪精度   
		DEFAULT_QUALITY, //指定输出质量   
		DEFAULT_PITCH | FF_SWISS, //指定字体的定位和外观    
		face);  
		HFONT hOldFont = (HFONT)SelectObject(wglGetCurrentDC(), hFont); 
		DeleteObject(hOldFont); 
}
void drawCNString(const char* str) {
	int len, i;
	wchar_t* wstring;
	HDC hDC = wglGetCurrentDC();
	GLuint list = glGenLists(1);
	// 计算字符的个数
	// 如果是双字节字符的（比如中文字符），两个字节才算一个字符
	// 否则一个字节算一个字符
	len = 0;
	for (i = 0; str[i] != '\0'; ++i)
	{
		if (IsDBCSLeadByte(str[i]))
			++i;
		++len;
	}
	// 将混合字符转化为宽字符
	wstring = (wchar_t*)malloc((len + 1) * sizeof(wchar_t));
	MultiByteToWideChar(CP_ACP, MB_PRECOMPOSED, str, -1, wstring, len);
	wstring[len] = L'\0';
	// 逐个输出字符
	for (i = 0; i<len; ++i)
	{
		wglUseFontBitmapsW(hDC, wstring[i], 1, list);
		glCallList(list);
	}

	// 回收所有临时资源
	free(wstring);
	glDeleteLists(list, 1);
}
void display_obstacle(){
	// OpenGL命令，用当前的绘图颜色绘制一个填充矩形
	//glRectf(-25.0f, 25.0f, 25.0f, -25.0f);
	for (int j = 0; j < obstacle_num; j++){
		if (obstacles[j].minus == 1) glColor3f(0.22f, 0.68f, 0.22f);
		else if (obstacles[j].minus==2) glColor3f(0.0f, 0.0f, 1.0f);
		else glColor3f(1.0f, 0.0f, 0.0f);
		glBegin(GL_LINE_LOOP);      //连续线段 且闭合
		for (int i = 0; i<n; ++i)
			glVertex2f(obstacles[j].position_x + obstacle_R*cos(2 * Pi / n*i), obstacles[j].position_y + obstacle_R*sin(2 * Pi / n*i));
		glEnd();
	}
	glFlush();
}
// 绘制场景
void RenderScene()
{
	// OpenGL命令，清除颜色缓冲区
	glClearColor(0.75, 0.75, 0.75, 0.0);
	glClear(GL_COLOR_BUFFER_BIT);
	glColor3f(0.0f, 0.0f, 0.0f);
	// OpenGL命令，用当前的绘图颜色绘制一个填充矩形
	//glRectf(-25.0f, 25.0f, 25.0f, -25.0f);
	glBegin(GL_LINE_LOOP);      //连续线段 且闭合
	for (int i = 0; i<n; ++i)
		glVertex2f(center_x+R*cos(2 * Pi / n*i), center_y+R*sin(2 * Pi / n*i));
	glEnd();
	char buffer[20];
	_itoa_s(score, buffer, 10);
	char one[100] = "分数:";
	strcat_s(one, buffer);
	//string	one = "分数：" + score;
	//char* p = const_cast<char*>(one.c_str());
	//one.copy(p,sizeof(one),0);
	//*(p + sizeof(one)) = '\0';
	display_obstacle();
	glColor3f(0.68f, 0.22f, 0.22f);
	glRasterPos2f(left_border-R,top_border+R-10);
	drawCNString(one);
	_itoa_s(life, buffer, 10);
	char two[100] = "生命：";
	strcat_s(two, buffer);
	glRasterPos2f(left_border - R, top_border + R - 20);
	drawCNString(two);
	_itoa_s(R, buffer, 10);
	char three[100] = "当前半径:";
	strcat_s(three, buffer);
	glRasterPos2f(left_border - R, top_border + R - 30);
	drawCNString(three);
	glFlush();
}
void SetupRC()
{
	// 设置用于清除窗口的颜色
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
}

// 当窗口大小改变时由GLUT函数库调用
void ChangeSize(GLsizei w, GLsizei h)
{
	//cout << "width: " << w << " height:" << h << endl;
	// 防止被0所除
	if (0 == h){
		h = 1;
	}
	// 设置视口为窗口的大小
	glViewport(0, 0, w, h);
	// 选择投影矩阵，并重置坐标系统
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	screenHeight = h;
	screenWidth = w;
	// 计算窗口的纵横比（像素比）
	Ratio = (GLfloat)w / (GLfloat)h;
	if (w <= h) {// 宽 < 高
		glOrtho(-100.0, 100.0, -100 / Ratio, 100 / Ratio, 1.0, -1.0);
		left_border = -100+R;
		right_border = 100-R;
		top_border = 100 / Ratio - R;
		bottom_border = -100 / Ratio + R;     //定义窗口边界
		obstacle_left_border = -100 + obstacle_R;
		obstacle_right_border = 100 - obstacle_R;
		obstacle_top_border = 100 / Ratio - obstacle_R;
		obstacle_bottom_border = -100 / Ratio + obstacle_R;     //定义窗口边界
	}
	else {// 宽 > 高
		glOrtho(-100.0 * Ratio, 100.0 *Ratio, -100.0, 100.0, 1.0, -1.0);
		left_border = -100*Ratio+R;
		right_border = 100*Ratio-R;
		top_border = 100-R	;
		bottom_border = -100+R;         //定义窗口边界
		obstacle_left_border = -100*Ratio + obstacle_R;
		obstacle_right_border = 100*Ratio - obstacle_R;
		obstacle_top_border = 100 - obstacle_R;
		obstacle_bottom_border = -100 + obstacle_R;     //定义窗口边界
	}
	// 选择模型视图矩阵，并重置坐标系统
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
//	if (center_x > right_border) center_x = right_border;    //边界判断  可以注释掉
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
		velocity[0] = -velocity[0];    //边界碰撞 碰撞时 速度反向 可以注释掉  可以用其他方法处理碰撞
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
		velocity[1] += cos;                      //sin cos是加速度1 水平方向和垂直方向的分量          
	}
	velocity[0] *= 0.98;
	velocity[1] *= 0.98;  //速度添加损耗
	if (pow(velocity[0], 2) + pow(velocity[1], 2) < 1 && d<2){ //速度过低 且鼠标与圆环距离较近时 停止
		velocity[0] = 0;
		velocity[1] = 0;
		center_x = gravity[0];
		center_y = gravity[1];            //停在鼠标点 可以注释掉
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
	if (center_x > right_border) center_x = right_border;    //边界判断  可以注释掉
	//if (center_y > top_border || center_y < bottom_border || center_x<left_border || center_x>right_border){
	//	while (true);
	//}
	glutPostRedisplay();
	Sleep(30);
}
void mouseMove(int x, int y){
	if (Ratio<=1) {// 宽 < 高
		gravity[0] = (x-screenWidth/2)*200/screenWidth;
		gravity[1] = (screenHeight/2 -y)*200/(screenHeight*Ratio);   //鼠标坐标变换
	}
	else {// 宽 > 高
		gravity[0] = (x - screenWidth/2) * 200 * Ratio / screenWidth;
		gravity[1] = (screenHeight/2 - y ) * 200 / screenHeight;
	}
}
int main(int argc, char *argv[])
{
	// 传递命令行参数，并对GLUT函数库进行初始化
	glutInit(&argc, argv);
	// 设置创建窗口时的显示模式（单缓冲区、RGBA颜色模式）
	glutInitDisplayMode(GLUT_SINGLE | GLUT_RGBA);
	glutInitWindowPosition(100, 100);
	glutInitWindowSize(screenWidth, screenHeight);
	// 创建窗口
	glutCreateWindow("小球");
	glLineWidth(3);
	srand((unsigned)time(0));
	selectFont(16, GB2312_CHARSET, "幼圆");
	// 设置显示回调函数
	glutDisplayFunc(RenderScene);
	glutIdleFunc(move);
	memset(obstacles, 0, sizeof(obstacles));
	// 设置当窗口的大小发生变化时的回调函数
	glutReshapeFunc(ChangeSize);
	glutPassiveMotionFunc(mouseMove);
	// 设置渲染状态
	SetupRC();
	glutMainLoop();

	return 0;
}
