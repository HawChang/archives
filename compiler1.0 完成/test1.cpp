#include <iostream>
#include "Astar.h"
using namespace std;

int main()
{
    int start_status[3][3]={{0}};
     for(int i=0;i<3;i++)
    {
        for(int j=0;j<3;j++)
        {
            cin>>start_status[i][j];
        }
    }
	double　a=2.31212e123;
	double b= -123.22e-23;
	double  c=12324.2.1;
	int d=2+3;
    node root=node(start_status);
    /*root.init_node(start_status,0);
    root.geth();
    root.setvalue();
    root.display();*/
    Astar solution=Astar(root);
    //solution.display();
	//xixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixixi
    solution.run();
    cout << "Hello world!" << endl;
    return 0;
}
