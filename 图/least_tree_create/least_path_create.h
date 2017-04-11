#ifndef GRAPH_H_INCLUDED
#define GRAPH_H_INCLUDED
#define MaxValue Int_Max //��<limits.h>��
#define NumEdges 50//������
#define NumVertices 10 //�������
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
    VertexData vexlist[NumVertices];       //�����
    EdgeData edge[NumVertices+1][NumVertices+1];//�ڽӾ��󡪱߱�, ����Ϊ��֮��Ĺ�ϵ
    int n,e;         //�������n�ͱ���e
public:
    void CreateMGragh();//����(����)ͼ���ڽӾ���
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
void MTGraph<VertexData,EdgeData>::CreateMGragh () //����(����)ͼ���ڽӾ���
{
    int i, j, k, w;
    cout<<"input the graph's numbers of nodes and edges:"<<endl;
    cin>>n>>e; //1.���붥�����ͱ���
    cout<<"input the vexlists:";
    for (i=1; i<=n; i++) //2.���붥����Ϣ�����������
        cin>>vexlist[i];
    for (i=1; i<=n; i++)
        for (j=1; j<=n; j++)
            edge[i][j]=0; //3.�ڽӾ����ʼ��
    cout<<"input the the value of the edge(i��j)"<<endl;
    for (k=0; k<e; k++)   //4.����e���߽����ڽӾ���
    {
        cout<<"edge #"<<k+1<<":";
        cin>>i>>j>>w; //����ߣ�i,j���ϵ�Ȩw
        if(i==j)
        {
            cout<<"can not connect with itsself."<<endl;
            k--;
        }
        else
        {
            edge[i][j]=w;
            edge[j][i]=w;     //��Ϊ����ͼ������ͼ��ɾ���þ仰
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
        }                  //����Ƿ����ظ�������ֱ���˳�
        n++;
        vexlist[n]=v;
        for(int i=0; i<=n; i++)
        {
            edge[i][n]=0;
            edge[n][i]=0;          //��Ϊ����ͼ������ͼ��ɾ���þ仰
        }
        cout<<"newnode success"<<endl;    //�����¶��������㣬��ʾ�й���
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
            cout<<"delnode success"<<endl;    //�ɹ�ɾȥ
            return;
        }
    }
    cout<<"can't find this node"<<endl;   //��ʾʧ��
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
            edge[j][i]=w;             //��Ϊ����ͼ������ͼ��ɾ���þ仰
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
        edge[j][i]=0;                  //��Ϊ����ͼ������ͼ��ɾ���þ仰
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
                }                  //ѡ��V-U�ĵ�
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
        }//ѡ������Ȩ��С�ı�(i,j)����i��U,j��(V-U)
        if(change)
        {
         //   cout<<"choose��   node"<<vexlist[temp1]<<"to"<<vexlist[temp2]<<"         the lowcost��"<<lowcost<<endl;
            N[++numofnode]=temp2;
            E[numofedge].from=temp1;
            E[numofedge].to=temp2;
            E[numofedge++].weight=edge[temp1][temp2];   //����ѡ������С��(I,j)����edge�߼���
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
