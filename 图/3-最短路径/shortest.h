#ifndef SHORTEST_H_INCLUDED
#define SHORTEST_H_INCLUDED
#include <iostream>
#include <string>
#include <windows.h>
using namespace std;
#define max_number 100
template<class T>
class adjgraph
{
    struct node
    {
        int position;
        int cost;
        struct node *next;
    };
    struct vertex
    {
        T data;
        node *first;
    };
    struct edge
    {
        bool exist;
        int E;
        int L;
        int cost;
        bool special;
    };
    vertex apex[max_number];   //n������0~n-1
    int n,e;
    void show_apex();
    bool edge_exist(int from,int to) const;
    void dfs_search_help(int position) const;
    void bfs_search_help(int position) const;
    void dijkstra_help(int position);
    int cost(int from, int to) const;
public:
    adjgraph()
    {
        n=0,e=0;
    }
    ~adjgraph() {};
    void create();
    void watch() const;
    int found(T x) const;
    void dfs_search(T x) const ;
    void bfs_search(T x) const ;
    bool topology() const;
    void dijkstra(T x);
    void special_road();

};
template <class T>
void adjgraph<T>::special_road()
{
    node *temp;
    edge edge_info[max_number][max_number];
    int VE[max_number]= {0},VL[max_number],L[max_number],E[max_number];
    int topo_info[max_number]= {-1},que[max_number]= {-1},f=0,l=0,indegree[max_number]= {0},nodes=0,v=0,num=0;

    for(int i=0; i<n; i++)
    {
        for(int j=0; j<n; j++)
        {
            edge_info[i][j].special=false;
            edge_info[i][j].exist=false;
        }
        temp=apex[i].first;
        while(temp)
        {
            //cout<<" to"<<" "<<apex[temp->position].data<<"("<<temp->cost<<")";
            edge_info[i][temp->position].exist=true;
            edge_info[i][temp->position].cost=temp->cost;
            temp=temp->next;
        }
    }            //��·��Ϣ����edge_info��
    for(int i=0; i<n; i++)
    {
        temp=apex[i].first;
        while(temp)
        {
            indegree[temp->position]++;
            temp=temp->next;
        }
    }
    for(int i=0; i<n; i++)
    {
        if(indegree[i]==0)
        {
            topo_info[num++]=i;
            que[l++]=i;
        }
    }
    while(f!=l)
    {
        v=que[f++];
        ++nodes;
        //cout<<"nodes #"<<++nodes<<":"<<apex[v].data<<endl;
        temp=apex[v].first;
        while(temp)
        {
            if(!(--indegree[temp->position]))
            {
                topo_info[num++]=temp->position;
                que[l++]=temp->position;
            }
            temp=temp->next;
        }
    }                       //��topo����Ϣ����topo_info��
    if(nodes<n)
        cout<<"there is a circle in the graph."<<endl;
    else
    {
//        cout<<"topo_info:"<<endl;
//        for(int i=0;i<n;i++)
//        {
//            cout<<apex[topo_info[i]].data<<" ";
//        }
//        cout<<endl;    //���topo�Ƿ���ȷ
        for(int i=1; i<n; i++) //��ԭ�㿪ʼ��VE
        {
            for(int j=0; j<n; j++)
            {
                if(edge_exist(j,topo_info[i]))
                {
                    v=VE[j]+cost(j,topo_info[i]);
                    if(v>VE[topo_info[i]]) VE[topo_info[i]]=v;
                }
            }
        }
        for(int i=0; i<n; i++)
        {
            VL[i]=99999;   //���޴�
        }
        VL[topo_info[n-1]]=VE[topo_info[n-1]];
        for(int i=n-2; i>=0; i--)
        {
            for(int j=0; j<n; j++)
            {
                if(edge_exist(topo_info[i],j))
                {
                    v=VL[j]-cost(topo_info[i],j);
                    if(v<VL[topo_info[i]]) VL[topo_info[i]]=v;
                }
            }
        }
//        cout<<"VE:"<<endl;
//        for(int i=0;i<n;i++)
//        {
//            cout<<apex[i].data<<" ";
//        }
//        cout<<endl;
//        for(int i=0;i<n;i++)
//        {
//            cout<<VE[i]<<" ";
//        }
//        cout<<endl;
//        cout<<"VL:"<<endl;
//        for(int i=0;i<n;i++)
//        {
//            cout<<apex[i].data<<" ";
//        }
//        cout<<endl;
//        for(int i=0;i<n;i++)
//        {
//            cout<<VL[i]<<" ";
//        }
//        cout<<endl;         //���VL[],VE[]
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<n; j++)
            {
                if(edge_info[i][j].exist)
                {
                    edge_info[i][j].E=VE[i];
                    edge_info[i][j].L=VL[j]-cost(i,j);
                    if(edge_info[i][j].E==edge_info[i][j].L) edge_info[i][j].special=true;
                    else edge_info[i][j].special=false;
                }
            }
        }
        cout<<"special road:"<<endl;
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<n; j++)
            {
                if(edge_info[i][j].special)
                {
                    cout<<"from :"<<apex[i].data<<" to :"<<apex[j].data<<" cost :"<<edge_info[i][j].cost<<endl;
                }
            }
        }


    }
}
template <class T>
int adjgraph<T>::cost(int from,int to) const
{
    node *temp=apex[from].first;
    while(temp&&temp->position!=to)
        temp=temp->next;
    return temp->cost;
}
template <class T>
void adjgraph<T>::dijkstra_help(int position)
{
    int confirm[max_number],cnumber=1,unsure[max_number],unumber=n-1,flag=0,temp;
    confirm[0]=position;
    int pos,to,m,total=0,temp_from,temp_to,num=0,edge_info[max_number*2];
    for(int i=0; i<position; i++)
    {
        unsure[i]=i;
    }
    for(int i=position+1; i<n; i++)
    {
        unsure[i-1]=i;
    }
    while(cnumber<n)
    {
//        cout<<"confirm :";
//         for(int i=0;i<cnumber;i++)
//        {
//            cout<<apex[confirm[i]].data<<" ";
//        }
//        cout<<endl;
//         cout<<"unsure :";
//         for(int i=0;i<unumber;i++)
//        {
//            cout<<apex[unsure[i]].data<<" ";
//        }
//        cout<<endl;
        flag=0,temp=99999;
        for(int i=0; i<cnumber; i++)
        {
//            cout<<"confirm :";
//         for(int i=0;i<cnumber;i++)
//        {
//            cout<<apex[confirm[i]].data<<" ";
//        }
//        cout<<endl;
//         cout<<"unsure :";
//         for(int i=0;i<unumber;i++)
//        {
//            cout<<apex[unsure[i]].data<<" ";
//        }
//        cout<<endl;
            for(int j=0; j<unumber; j++)
            {
                if(edge_exist(confirm[i],unsure[j]))
                {
                    flag=1;
                    m=cost(confirm[i],unsure[j]);
                    if(temp>m)
                    {
                        temp_from=confirm[i];
                        temp_to=unsure[j];
                        pos=j;
                        to=unsure[j];
                        temp=m;
                    }
                }
            }
        }
        if(flag)
        {
            edge_info[num++]=temp_from;
            edge_info[num++]=temp_to; //һ������ ��������ʼ�� ˫��ȡ�����
            confirm[cnumber++]=to;
            total+=temp;
            for(int i=pos; i<unumber-1; i++)
            {
                unsure[i]=unsure[i+1];
            }
            unumber--;
        }
        else
            break;
    }
    if(cnumber<n)
        cout<<"fail..."<<endl;
    else
    {
//        for(int i=0;i<n;i++)
//        {
//            cout<<apex[confirm[i]].data<<" ";
//        }
//        cout<<endl;
        for(int i=0; i<num; i+=2)
        {
            cout<<apex[edge_info[i]].data<<" to "<<apex[edge_info[i+1]].data<<" cost:"<<cost(edge_info[i],edge_info[i+1])<<endl;
        }
        cout<<"total:"<<total<<endl;
    }
    /**<
    ��node *���� һ����ȷ�� һ���ѡ
    while(node<n)
    {
    flag=0��
    for(ѡ����һ��x��
    {
        for��δѡ����һ��y��
        {
        if������x->y�� {
            flag=1��
            if��weight��x->y������С
            ��from=x��to=y��weight=x->y��}
        }
        if��flag=0�� goto end��
    }
    ��y����xһ�� node++��
    }
    if��node<n�� ʧ��
     */
}
template <class T>
void adjgraph<T>::dijkstra(T x)
{
    int t=found(x);
    if(t==-1) cout<<"the node "<<x<<" dosen't exist."<<endl;
    else
    {
        cout<<"dijkstra from apex "<<x<<":"<<endl;
        dijkstra_help(t);
    }
}
template <class T>
bool adjgraph<T>::topology() const
{
    node *temp;
    int que[max_number]= {-1},f=0,l=0,indegree[max_number]= {0},nodes=0,v;
    for(int i=0; i<n; i++)
    {
        temp=apex[i].first;
        while(temp)
        {
            indegree[temp->position]++;
            temp=temp->next;
        }
    }
    for(int i=0; i<n; i++)
    {
        if(indegree[i]==0) que[l++]=i;
    }
    cout<<"topology:"<<endl;
    while(f!=l)
    {
        v=que[f++];
        cout<<"nodes #"<<++nodes<<":"<<apex[v].data<<endl;
        temp=apex[v].first;
        while(temp)
        {
            if(!(--indegree[temp->position]))
                que[l++]=temp->position;
            temp=temp->next;
        }
    }
    if(nodes<n)
        cout<<"there is a circle in the graph."<<endl;
}
/**<
5 8
abcdf
a b 5
a c 4
d b 7
c b 6
c d 4
c f 8
b f 9
f d 1

 */
template <class T>
void adjgraph<T>::dfs_search_help(int position) const
{
    int l=0,i,order=1,flag;
    //vertex *t=position;
    node *temp=apex[position].first,*temp2;
    int mark[max_number]= {0};
    int stck[max_number];
    mark[position]=1;
    while(1)  //������ջ��Ϊ��־ ���������Ѿ��������� ����Ͳ����ж���
    {
        if(temp&&mark[temp->position]!=1)
        {
            cout<<"num."<<order++<<":"<<apex[temp->position].data<<endl;
            mark[temp->position]=1;
            if(temp->next)
            {
                temp2=temp->next;
                while(temp2)
                {
                    //flag=1;
                    // for(int i=0;i<l;i++)
                    //  {
                    //     if(stck[i]==temp2->position) flag=0;
                    //}
                    // if(mark[temp2->position]!=1&&flag)
                    stck[l++]=temp2->position;
                    temp2=temp2->next;
                }
            }
            temp=apex[temp->position].first;
        }
        else
        {
            if(temp)
            {
                temp2=temp->next;
                while(temp2)    //��ֹ��Щ���ں������Ϊǰ����ѷ���δ��ѹ��ջ
                {
                    flag=1;
                    for(int i=0; i<l; i++)
                    {
                        if(stck[i]==temp2->position) flag=0;
                    }
                    if(mark[temp2->position]!=1&&flag)
                        stck[l++]=temp2->position;
                    temp2=temp2->next;
                }
            }
            if(--l>=0)
            {
                if(mark[stck[l]]!=1)
                {
                    cout<<"num."<<order++<<":"<<apex[stck[l]].data<<endl;
                    mark[stck[l]]=1;
                }
                temp=apex[stck[l]].first;
            }
            else
                break;
        }
        /**
        while(ջ��Ϊ��)
        {
            �����tempָ�벻Ϊ�գ�
            {
                ����δ����
                ����tempָ������
                �����tempָ���position���ڽӵ㣩
                {
                    ���temp��next!=null
                    ��ѹ��ӵڶ�����������ڽӵ�
                }
                temp��Ϊapex[temp.position].first;
            }
            else
            {
            ���ջ��Ϊ��
            ջ��Ԫ��δ���� �����
            ����ջ����temp=stck[--l];
            }
        }
        */
//        for(int i=0;i<l;i++)
//        {
//            cout<<i<<" ";
//        }
//        cout<<endl;
//        for(int i=0;i<l;i++)
//        {
//            cout<<apex[stck[i]].data<<" ";
//        }
//        cout<<endl;
//        for(int i=0;i<n;i++)
//        {
//            cout<<"mark["<<i<<"]";
//        }
//        cout<<endl;
//        for(int i=0;i<n;i++)
//        {
//            cout<<mark[i]<<" ";
//        }
//        cout<<endl;
    }
}
template<class T>
void adjgraph<T>::dfs_search(T x) const
{
    if(found(x)==-1) cout<<"the node "<<x<<" dosen't exist."<<endl;
    else
    {
        cout<<"dfs search from apex "<<x<<":"<<endl;
        dfs_search_help(found(x));
    }
}
template <class T>
void adjgraph<T>::bfs_search_help(int position) const
{
    int f=0,l=0,i,order=1;
    node *temp;
    int mark[max_number]= {0};
    int que[max_number];
    mark[position]=1;
    que[l++]=position;
    while(f!=l)
    {
        i=que[f++];
        temp=apex[i].first;
        while(temp)
        {
            if(!mark[temp->position])
            {
                cout<<"num."<<order++<<":"<<apex[temp->position].data<<endl;
                mark[temp->position]=1;
                que[l++]=temp->position;
            }
            temp=temp->next;
        }
    }
}
template<class T>
void adjgraph<T>::bfs_search(T x) const
{
    if(found(x)==-1) cout<<"the node "<<x<<" dosen't exist."<<endl;
    {
        cout<<"bfs search from apex "<<x<<":"<<endl;
        bfs_search_help(found(x));
    }
}
template<class T>
bool adjgraph<T>::edge_exist(int from, int to) const   //������·��Ϣ��ʱ���ѯ�Ƿ��Ѿ�������·��
{
    // cout<<"from:"<<from<<"  to:"<<to<<endl;
    if(from==to) return false;
    node *temp=apex[from].first;
    while(temp)
    {
        //    cout<<"temp->position:"<<temp->position<<endl;
        if(to==temp->position) return true;
        temp=temp->next;
    }
    return false;
}
template <class T>
void adjgraph<T>::show_apex()                 //��ʾ�������еĽڵ�ֵ
{
//    for(int i=0;i<n;i++)
//        cout<<i<<" ";
//    cout<<endl;
    for(int i=0; i<n; i++)
        cout<<apex[i].data<<" ";
    cout<<endl;
}
template <class T>
int adjgraph<T>::found(T x) const          //�ҵ���ֵ�ڽڵ������е�λ�ã����ڵ�ֵת��Ϊint postion�Ա��ڲ���
{
    for(int i=0; i<n; i++)
    {
        if(x==apex[i].data)
            return i;
    }
    return -1;
}
template<class T>
void adjgraph<T>::watch() const         //�۲��ڽӱ�
{
    node *temp= new node;
    for(int i=0; i<n; i++)
    {
        cout<<"apex #"<<i<<":"<<apex[i].data;
        temp=apex[i].first;
        while(temp)
        {
            cout<<" to"<<" "<<apex[temp->position].data<<"("<<temp->cost<<")";
            temp=temp->next;
        }
        cout<<endl;
    }
}
template<class T>
void adjgraph<T>::create()    //���ڽӱ�
{
    int weight,f,t;
    T from,to;
    cout<<"input the number of the apex and the edge:"<<endl<<">>";
    cin>>n>>e;
    for(int i=0; i<n; i++) apex[i].data=NULL;   //��ʼ����������
    system("cls");
    for(int i=0; i<n; i++)
    {
        cout<<"input the data of the node #"<<i+1<<endl<<">>";
        cin>>to;
        if(-1==found(to))
        {
            apex[i].data=to;
            apex[i].first=NULL;
            system("cls");
            show_apex();
        }
        else
        {
            cout<<"the node has already existed."<<endl;
            i--;
        }
    }
    for(int i=0; i<e; i++)
    {
        cout<<"input the information of the edge #"<<i+1<<":"<<endl
            <<"(a b 3)means edge from a to b weight 3."<<endl<<">>";
        cin>>from>>to>>weight;
        if(weight<0) cout<<"weight must be above 0."<<endl;
        else
        {
            f=found(from);
            t=found(to);
            if(f==-1||t==-1)
            {
                if(f==-1) cout<<"node "<<from<<" doesn't exist."<<endl;
                if(t==-1) cout<<"node "<<to<<" doesn't exist."<<endl;
                i--;
            }
            else if(edge_exist(f,t))
            {
                cout<<"the edge has alredy existed."<<endl;
                i--;
            }
            else
            {
                node *temp=new node;
                temp->cost=weight;
                temp->next=apex[f].first;
                apex[f].first=temp;
                temp->position=t;
                system("cls");
                watch();
            }
        }
    }

}

#endif // SHORTEST_H_INCLUDED
