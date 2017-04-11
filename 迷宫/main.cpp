#include <iostream>
using namespace std;
#include <stdio.h>
#include <stdlib.h>
#define maxlength 1000
#define maxl 100
//#define row 10
//#define col 10
typedef struct
{
    int hang;
    int lie;
}position;
typedef position elementtype;
class stck
{
public:
    elementtype elements[maxlength];
    int top;
    void makenull();
    int emptystck();
    elementtype tops();
    void pop();
    void push(elementtype x);
};
void stck::makenull()
{
    top=maxlength;
}
int stck::emptystck()
{
    if(top>maxlength-1)
        return 1;
    else return 0;
}
elementtype stck::tops()
{
    if(emptystck());
      else  return (elements[top]);
}
void stck::pop()
{
    if(emptystck())
    {
        printf("栈为空。\n");
        exit (1);
    }
    else top=top+1;
}
void stck::push(elementtype x)
{
    if(top==0)
    {
        printf("栈为满。\n");
        exit (1);
    }
    else
    {
        top=top-1;
        elements[top]=x;
    }
}
int main()
{
    int matrix[maxl][maxl];
    //*matrix[0][0]=(int *)malloc(1000000*sizeof(int));
    stck s;
    int dir[4][2]={{0,1},{0,-1},{-1,0},{1,0}};
    int i,j,step=0,row,col;
    position now;
    position next;
    cout<<"input the number of the rows and the cols(2*2~100*100):";
    cin>>row>>col;
    row+=2,col+=2;
    cout<<"input the matrix:"<<endl;
    for(j=0;j<col;j++)
    {
        matrix[0][j]=1;
        matrix[row-1][j]=1;
    }
    for(i=1;i<row-1;i++)
    {
        matrix[i][0]=1;
        matrix[i][col-1]=1;
    }
    for(i=1;i<row-1;i++)
    {
        for(j=1;j<col-1;j++)
        {
            scanf("%d",&matrix[i][j]);
        }
    }
    s.makenull();
    now.hang=1;
    now.lie=1;
    s.push(now);
    matrix[1][1]=1;
    while((now.hang-row+2)||(now.lie-col+2))     //判定是否走到迷宫出口
    {
         printf("第%d步:\n",step);
        for(i=0;i<row;i++)
        {
        for(j=0;j<col;j++)
        {
            printf("%d ",matrix[i][j]);
        }                                 //显示迷宫搜索各步骤方便检查
        printf("\n");
        }
        for(i=0;i<4;i++)
        {
            step++;
            next.hang=now.hang+dir[i][0];             //搜索现在位置的周边
            next.lie=now.lie+dir[i][1];
            if(!matrix[next.hang][next.lie])         //若现在位置周边有位置未标记
            {
                s.push(next);                           //将该位置压栈
                now=next;
                matrix[now.hang][now.lie]=2;
                break;//将该位置作为现在位置，并赋值为2，重复while搜索下一个。
            }
        }
        if(i==4)                       		//若现在位置四周均已标记
        {
            now=s.tops();

            s.pop();                //弹栈，现在位置回退。
            if(s.emptystck())		//若栈已为空，说明迷宫无解。
            {
                printf("失败。\n");
                exit (1);
            }
        }

    }
    printf("成功。\n");
    return 0;
}
