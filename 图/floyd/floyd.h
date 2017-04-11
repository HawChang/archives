#ifndef FLOYD_H_INCLUDED
#define FLOYD_H_INCLUDED
#define max_number 100
#define infinit 99999
#include <iostream>
#include <iomanip>
using namespace std;

template<class T>
class floyd
{
    struct edgeinf
    {
        int from,to;
        int weight;
    };
    int a[max_number+1][max_number+1],p[max_number+1][max_number+1];
    int  E[max_number];
    T vexlist[max_number];       //顶点表
    int edge[max_number+1][max_number+1];//邻接矩阵—边表, 可视为边之间的关系
    int n,e;
public:
//    int found(T a);
    void create();
    void display();
    void floyd_help();
//    void find_path(T a,T b);
};
//template <class T>
//int floyd<T>::found(T a)
//{
//    for(int i=1;i<=n;i++)
//    {
//        if(a==vexlist[i]) return i;
//    }
//    return -1;
//}
//template <class T>
//void floyd<T>::find_path(T a,T b)
//{
//    int stck[max_number],l=-1;
//    int m,n;
//    m=found(a);
//    n=found(b);
//    while(p[m][n]!=0)
//    {
//        cout<<"stck["<<l<<"++]="<<n<<endl;
//        stck[++l]=n;
//        n=p[m][n];
//    }
//    cout<<"path: "<<a;
//    while(l!=-1)
//    {
//        cout<<"->"<<vexlist[stck[--l]]<<endl;
//    }
//    cout<<endl;
//}

template <class T>
void floyd<T>::floyd_help()
{
    for(int i=1;i<=n;i++)
    {
        for(int j=1;j<=n;j++)
        {
            a[i][j]=edge[i][j];
            p[i][j]=0;
        }
    }
    for(int k=1;k<=n;k++)
    {
        for(int i=1;i<=n;i++)
        {
            for(int j=1;j<=n;j++)
            {
                if(a[i][k]+a[k][j]<a[i][j])
                {
                    a[i][j]=a[i][k]+a[k][j];
                    p[i][j]=k;
                }
            }
        }
    }
    cout<<"a[][]:"<<endl;
    cout<<"  ";
    for(int i=1; i<=n; i++)
        cout<<setw(5)<<vexlist[i]<<" ";
    cout<<endl;
     for(int j=1; j<=n; j++)
    {
        cout<<vexlist[j]<<":";
        for(int k=1; k<=n; k++)
        {
            cout<<setw(5)<<a[j][k]<<" ";
        }
        cout<<endl;
    }
    cout<<"p[][]:"<<endl;
    cout<<"  ";
    for(int i=1; i<=n; i++)
        cout<<setw(5)<<vexlist[i]<<" ";
    cout<<endl;
     for(int j=1; j<=n; j++)
    {
        cout<<vexlist[j]<<":";
        for(int k=1; k<=n; k++)
        {
            cout<<setw(5)<<p[j][k]<<" ";
        }
        cout<<endl;
    }

}
template <class T>
void floyd<T>::create()//建立(无向)图的邻接矩阵
{
    int i, j, k, w;
    cout<<"input the graph's numbers of nodes and edges:"<<endl;
    cin>>n>>e; //1.输入顶点数和边数
    cout<<"input the vexlists:";
    for (i=1; i<=n; i++) //2.读入顶点信息，建立顶点表
        cin>>vexlist[i];
    for (i=1; i<=n; i++)
        for (j=1; j<=n; j++)
            edge[i][j]=infinit; //3.邻接矩阵初始化
    cout<<"input the the value of the edge(i，j)"<<endl;
    for (k=0; k<e; k++)   //4.读入e条边建立邻接矩阵
    {
        cout<<"edge #"<<k+1<<":";
        cin>>i>>j>>w; //输入边（i,j）上的权w
        if(i==j)
        {
            cout<<"can not connect with itsself."<<endl;
            k--;
        }
        else if(w<0)
        {
            cout<<"edge weight can not be negative."<<endl;
            k--;
        }
        else
        {
            edge[i][j]=w;
        }
        display();
    }
}
template <class T>
void floyd<T>::display()
{
    cout<<"  ";
    for(int i=1; i<=n; i++)
        cout<<vexlist[i]<<" ";
    cout<<endl;
    for(int j=1; j<=n; j++)
    {
        cout<<vexlist[j]<<":";
        for(int k=1; k<=n; k++)
        {
            cout<<setw(5)<<edge[j][k]<<" ";
        }
        cout<<endl;
    }
}

#endif // FLOYD_H_INCLUDED
