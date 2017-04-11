#include <iostream>
#include "least_path_create.h"
typedef char dingdianleixin;
typedef int roadweight;
using namespace std;
int main()
{
    int choose;
    roadweight w;
    char f,v1,v2;
    MTGraph<dingdianleixin,roadweight> graph;
    graph.CreateMGragh();    //创建基本邻接图
    graph.Display();
    do
    {
        cout<<"--------------choose functions------------------------------------------------"<<endl;
        cout<<"1.insrt new node  2.del node  3.create new path  4.del path  5.search the nodes beside   6.judge the path   7.choose the least tree by prim  8.choose the least tree by Kruskal   others.exit"<<endl;
        cin>>choose;
        switch(choose)
        {
        case 1:
            cout<<"input the new node:";
            cin>>v1;
            graph.NewNode(v1);
            graph.Display();
            break;
        case 2:
            cout<<"input the node you want to del:";
            cin>>v1;
            graph.DelNode(v1);
            graph.Display();
            break;
        case 3:
            cout<<"input the path's two nodes and the value:";
            cin>>v1>>v2>>w;
            graph.SetSucc(v1,v2,w);
            graph.Display();
            break;
        case 4:
            cout<<"input the path's two nodes to del the path:";
            cin>>v1>>v2;
            graph.DelSucc(v1,v2);
            graph.Display();
            break;
        case 5:
            cout<<"ipnut the nodes you want to search:";
            cin>>v1;
            graph.List_of_Node_Succ(v1);
            graph.Display();
            break;
        case 6:
            cout<<"input the two nodes of the path you want to create and the value:";
            cin>>v1>>v2>>w;
            graph.Isedge(v1,v2,w);
            graph.Display();
            break;
        case 7:
            graph.prim();
            break;
        case 8:
            graph.Kruskal();
            break;
        default:
            return 0;
        }
    }
    while(1);
    return 0;
}
