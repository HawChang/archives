#ifndef GRAPH_H_INCLUDED
#define GRAPH_H_INCLUDED
#define MaxValue Int_Max //在<limits.h>中
#define NumEdges 50//边条数
#define NumVertices 10 //顶点个数
using namespace std;
template <class VertexData,class EdgeData>
class MTGraph
{
    struct linjie
    {
        int length;
        VertexData besides[NumVertices];
    };
    linjie B[NumVertices+1];
    struct edgeinf
    {
        int from,to;
        EdgeData weight;
    };
    int parent[NumVertices+1];
    edgeinf E[NumEdges];
    VertexData vexlist[NumVertices];       //顶点表
    EdgeData edge[NumVertices+1][NumVertices+1];//邻接矩阵—边表, 可视为边之间的关系
    int n,e;         //顶点个数n和边数e
public:
    void CreateMGragh();//建立(无向)图的邻接矩阵
    void NewNode(VertexData v);
    void DelNode(VertexData v);
    void Display();
    void SetSucc(VertexData v1,VertexData v2,EdgeData w);
    void DelSucc(VertexData v1,VertexData v2);
    void List_of_Node_Succ(VertexData v);
    bool Isedge(VertexData v1,VertexData v2,EdgeData w);
    void prim();
    void Kruskal();
};
template <class VertexData,class EdgeData>
void MTGraph<VertexData,EdgeData>::CreateMGragh () //建立(无向)图的邻接矩阵
{
    int i, j, k, w;
    cout<<"input the graph's numbers of nodes and edges:"<<endl;
    cin>>n>>e; //1.输入顶点数和边数
    cout<<"input the vexlists:";
    for (i=1; i<=n; i++) //2.读入顶点信息，建立顶点表
        cin>>vexlist[i];
    for (i=1; i<=n; i++)
        for (j=1; j<=n; j++)
            edge[i][j]=0; //3.邻接矩阵初始化
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
        else
        {
            edge[i][j]=w;
            edge[j][i]=w;     //此为无向图，有向图则删除该句话
        }
    }
}
template <class VertexData,class EdgeData>
void MTGraph<VertexData,EdgeData>::NewNode(VertexData v)
{
    if(n==NumVertices)
    {
        cout<<"the graph is full."<<endl;
    }
    else
    {
        for(int i=0; i<=n; i++)
        {
            if(v==vexlist[i])
            {
                cout<<"already exsit"<<endl;
                return;
            }
        }                  //检查是否有重复，有则直接退出
        n++;
        vexlist[n]=v;
        for(int i=0; i<=n; i++)
        {
            edge[i][n]=0;
            edge[n][i]=0;          //此为无向图，有向图则删除该句话
        }
        cout<<"newnode success"<<endl;    //加入新顶点且清零，表示承购。
    }
}
template <class VertexData,class EdgeData>
void MTGraph<VertexData,EdgeData>::DelNode(VertexData v)
{
    int i,j,k;
    for(i=0; i<=n; i++)
    {
        if(v==vexlist[i])
        {
            for(j=1; j<i; j++)
            {
                for(k=i; k<n; k++)
                {
                    edge[j][k]=edge[j][k+1];
                    edge[k][j]=edge[k+1][j];
                }
            }
            for(j=i; j<n; j++)
            {
                for(k=i; k<n; k++)
                {
                    edge[j][k]=edge[j+1][k+1];
                }
            }
            for(j=i; j<n; j++)
            {
                vexlist[j]=vexlist[j+1];
            }
            n--;
            cout<<"delnode success"<<endl;    //成功删去
            return;
        }
    }
    cout<<"can't find this node"<<endl;   //表示失败
}
template <class VertexData,class EdgeData>
void MTGraph<VertexData,EdgeData>::Display()
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
            cout<<edge[j][k]<<" ";
        }
        cout<<endl;
    }
}
template <class VertexData,class EdgeData>
void MTGraph<VertexData,EdgeData>::SetSucc(VertexData v1,VertexData v2,EdgeData w)
{
    int i,j,flag1=0,flag2=0;
    for(i=1; i<=n; i++)
        if(vexlist[i]==v1)
        {
            flag1=1;
            break;
        }
    for(j=1; j<=n; j++)
        if(vexlist[j]==v2)
        {
            flag2=1;
            break;
        }
    if(flag1*flag2)
    {
        if(edge[i][j]==w)
            cout<<"already has"<<endl;
        else
        {
            edge[i][j]=w;
            edge[j][i]=w;             //此为无向图，有向图则删除该句话
            cout<<"setsucc success"<<endl;
        }
    }
    else
    {
        cout<<"input the wrong node"<<endl;
    }
}
template <class VertexData,class EdgeData>
void MTGraph<VertexData,EdgeData>::DelSucc(VertexData v1,VertexData v2)
{
    int i,j,flag1=0,flag2=0;
    for(i=1; i<=n; i++)
        if(vexlist[i]==v1)
        {
            flag1=1;
            break;
        }
    for(j=1; j<=n; j++)
        if(vexlist[j]==v2)
        {
            flag2=1;
            break;
        }
    if(flag1*flag2)
    {

        edge[i][j]=0;
        edge[j][i]=0;                  //此为无向图，有向图则删除该句话
        cout<<"delsucc success"<<endl;
    }
    else
    {
        cout<<"ipnut the wrong node"<<endl;
    }
}
template <class VertexData,class EdgeData>
void MTGraph<VertexData,EdgeData>::List_of_Node_Succ(VertexData v)
{
    int i,flag=0,k=0;
    for(i=1; i<=n; i++)
        if(vexlist[i]==v)
        {
            flag=1;
            break;
        }
    if(flag)
    {
        B[i].length=0;
        for(int j=1; j<=n; j++)
        {
            if(edge[i][j])
            {
                // cout<<G->vexlist[j]<<" ";
                B[i].besides[B[i].length++]=vexlist[j];
            }
        }
        //cout<<endl;
        cout<<"search nodebesides success"<<endl;
        cout<<"besides is:";
        for(int j=0; j<B[i].length; j++)
            cout<<B[i].besides[j]<<" ";
        cout<<endl;
    }
    else
    {
        cout<<"the node does not exist."<<endl;
    }
}
template <class VertexData,class EdgeData>
bool MTGraph<VertexData,EdgeData>::Isedge(VertexData v1,VertexData v2,EdgeData w)
{
    int i,j,flag1=0,flag2=0;
    for(i=1; i<=n; i++)
        if(vexlist[i]==v1)
        {
            flag1=1;
            break;
        }
    for(j=1; j<=n; j++)
        if(vexlist[j]==v2)
        {
            flag2=1;
            break;
        }
    if(flag1*flag2&&edge[i][j])
    {
        cout<<"the path exist, ";
        if(edge[i][j]==w)
        {
            cout<<"and the value is right."<<endl;
            return true;
        }
        else
        {
            cout<<"but the valu eis wrong."<<endl;
            return false;
        }
    }
    else if(!edge[i][j])
    {
        cout<<"the path does not exist."<<endl;
        return false;
    }
    else
    {
        cout<<"input the wrong node."<<endl;
        return false;
    }
}
template <class VertexData,class EdgeData>
void MTGraph<VertexData,EdgeData>::prim()
{
    int N[NumVertices+1],numofnode=1,numofedge=0,flag,temp1,temp2,temp=0,change=0;
    EdgeData lowcost;
    N[1]=1;
    while(numofnode<n)
    {
        lowcost=9999999;
        for(int i=1; i<=numofnode; i++)
        {
            for(int j=1; j<=n; j++)
            {
                flag=1;
                for(int k=1; k<=numofnode; k++)
                {
                    if(j==N[k])
                    {
                        flag=0;
                        break;
                    }
                }                  //选出V-U的点
                if(flag)
                {
                    if(edge[N[i]][j])
                    {
                        if(lowcost>edge[N[i]][j])
                        {
                            change=1;
                            lowcost=edge[N[i]][j];
                            temp1=N[i];
                            temp2=j;
                        }
         //               cout<<"node:"<<N[i]<<"   "<<"j="<<j<<"   lowcost="<<lowcost<<endl;
                    }
                }
            }
        }//选出现有权最小的边(i,j)，且i∈U,j∈(V-U)
        if(change)
        {
         //   cout<<"choose：   node"<<vexlist[temp1]<<"to"<<vexlist[temp2]<<"         the lowcost："<<lowcost<<endl;
            N[++numofnode]=temp2;
            E[numofedge].from=temp1;
            E[numofedge].to=temp2;
            E[numofedge++].weight=edge[temp1][temp2];   //将刚选出的最小边(I,j)加入edge边集。
            temp=temp+edge[temp1][temp2];
            change=0;
        }
        else
            break;
    }
    Display();
    if(numofnode<n)
    {
        cout<<"the graph is not all connected"<<endl;
        cout<<numofedge<<" node(s) have(s) already chooose"<<endl;
        for(int i=0; i<numofedge; i++)

        {
            cout<<"from:"<<vexlist[E[i].from]<<"  to:"<<vexlist[E[i].to]<<"   value:"<<E[i].weight<<endl;
        }
        cout<<"total value:"<<temp<<endl;    //value just suit the int
    }
    else
    {
        cout<<"create success."<<endl;
        for(int i=0; i<numofedge; i++)

        {
            cout<<"from:"<<vexlist[E[i].from]<<"  to:"<<vexlist[E[i].to]<<"   value:"<<E[i].weight<<endl;
        }
        cout<<"total value:"<<temp<<endl;
    }

}
/**value just suit the int
 */
template <class VertexData,class EdgeData>
void MTGraph<VertexData,EdgeData>::Kruskal()
{
    edgeinf temp[NumEdges+1],order[NumEdges+1];
    int numofedge=0,edgechosen=0,num=0,numoforder,value=0,tem;
    for(int i=1; i<=NumVertices; i++) parent[i]=i;
    for(int i=1; i<=n; i++)
    {
        for(int j=i; j<=n; j++)
        {
            if(edge[i][j])
            {
                order[numofedge].weight=0;
                temp[numofedge].from=i;
                temp[numofedge].to=j;
                temp[numofedge++].weight=edge[i][j];
           //     cout<<i<<","<<j<<":"<<temp[numofedge-1].weight<<endl; //check
            }
        }
    }
    for(int i=0; i<numofedge; i++)
    {
       // cout<<"initialing..."<<endl;                 //check
        numoforder=0;
        while(temp[i].weight>order[numoforder].weight&&order[numoforder].weight!=0)
        {
          // cout<<temp[i].weight<<"<"<<order[numoforder].weight<<endl;        //check
            numoforder++;
        }
        num++;
        for(int j=num; j>numoforder; j--)
        {
            order[j].from=order[j-1].from;
            order[j].to=order[j-1].to;
            order[j].weight=order[j-1].weight;
        }
        order[numoforder].from=temp[i].from;
        order[numoforder].to=temp[i].to;
        order[numoforder].weight=temp[i].weight;         //order the edges into order
      //  for(int i=0;i<numoforder;i++)
     //   {
      //      cout<<i+1<<":"<<order[i].weight<<endl;
     //   }
    }
    /**
      if the edges is not enough,choose the least-cost edge and the two nodes must belong to different parts
     or choose the next least edge. if all the edges is searched then check the nodes. the
     >>>>>>>>>>USE E[] to store the edge chosen<<<<<<<<<<<
     */
    for(int i=0; i<numofedge&&edgechosen<n; i++)
    {
       // cout<<"creating..."<<endl;
        if(parent[order[i].from]!=parent[order[i].to])
        {
           // for(int k=1;k<=n;k++)
          //  {
          //      cout<<parent[k]<<" ";
          //  }
           // cout<<endl;
           // for(int k=1;k<=n;k++)
          //  {
           //     cout<<k<<" ";
          //  }
           // cout<<endl;
           // cout<<parent[order[i].from]<<"!="<<parent[order[i].to];
            E[edgechosen].from=order[i].from;
            E[edgechosen].to=order[i].to;
            E[edgechosen++].weight=order[i].weight;
            tem=parent[order[i].to];
            for(int k=1;k<=n;k++)
            {
                if(parent[k]==tem)
                {
                    parent[k]=parent[order[i].from];
                }
            }
            value+=order[i].weight;
            //cout<<"edgechosen:"<<edgechosen<<"n:"<<n<<endl;
        }
    }
    Display();
    if(edgechosen<n-1)
    {
      //  cout<<"edgechosen:"<<edgechosen<<"n:"<<n<<endl;
        cout<<"the graph is not all connected"<<endl;
        cout<<edgechosen<<" node(s) have(s) already chooose"<<endl;
        for(int i=0; i<edgechosen; i++)
        {
            cout<<"from:"<<vexlist[E[i].from]<<"  to:"<<vexlist[E[i].to]<<"   value:"<<E[i].weight<<endl;
        }
        cout<<"total value:"<<value<<endl;
    }
    else
    {
        cout<<"create success."<<endl;
        for(int i=0; i<edgechosen; i++)
        {
            cout<<"from:"<<vexlist[E[i].from]<<"  to:"<<vexlist[E[i].to]<<"   value:"<<E[i].weight<<endl;
        }
        cout<<"total value:"<<value<<endl;
    }
}

#endif // GRAPH_H_INCLUDED
