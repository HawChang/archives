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
        printf("ջΪ�ա�\n");
        exit (1);
    }
    else top=top+1;
}
void stck::push(elementtype x)
{
    if(top==0)
    {
        printf("ջΪ����\n");
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
    while((now.hang-row+2)||(now.lie-col+2))     //�ж��Ƿ��ߵ��Թ�����
    {
         printf("��%d��:\n",step);
        for(i=0;i<row;i++)
        {
        for(j=0;j<col;j++)
        {
            printf("%d ",matrix[i][j]);
        }                                 //��ʾ�Թ����������跽����
        printf("\n");
        }
        for(i=0;i<4;i++)
        {
            step++;
            next.hang=now.hang+dir[i][0];             //��������λ�õ��ܱ�
            next.lie=now.lie+dir[i][1];
            if(!matrix[next.hang][next.lie])         //������λ���ܱ���λ��δ���
            {
                s.push(next);                           //����λ��ѹջ
                now=next;
                matrix[now.hang][now.lie]=2;
                break;//����λ����Ϊ����λ�ã�����ֵΪ2���ظ�while������һ����
            }
        }
        if(i==4)                       		//������λ�����ܾ��ѱ��
        {
            now=s.tops();

            s.pop();                //��ջ������λ�û��ˡ�
            if(s.emptystck())		//��ջ��Ϊ�գ�˵���Թ��޽⡣
            {
                printf("ʧ�ܡ�\n");
                exit (1);
            }
        }

    }
    printf("�ɹ���\n");
    return 0;
}
