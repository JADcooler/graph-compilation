import java.util.*;
public class Main
{
    static char [][]graph=new char[1001][1001];
    static char [][]graph2=new char[1001][1001];
    static char [][]edge=new char[1001][1001];

    static int []seen=new int[100];
    static int []level=new int[100];
    static int []parent=new int[1000];

    static int[]prenumber=new int[100];
    static int[]postnumber= new int[100];
    static int clock=0;
    static int dim=0;

    static int backedge=0;
    static int crossedge=0;
    static int forwardedge=0;
    static int treeedge=0;

    static int hasloop=0;

    static void display()
    {
        System.out.printf("\n");
        for(int i=0;i<dim;++i)System.out.printf(" \t%d",i);
        System.out.printf("\n");
        for(int i=0;i<dim;++i)System.out.printf(" \t_");
        System.out.printf("\n");
        for(int i=0;i<dim;++i)
        {
            System.out.printf("%d:",i);
            for(int j=0;j<dim;++j)
            {
                System.out.printf("\t%c",graph[i][j]);
            }
            System.out.printf("\n");
        }
    }
    static void bfs(int a)
    {
        Queue<Integer> Q= new LinkedList<>();
        Q.add(a);
        //System.out.printf("%d , ",a);seen[a]=1;

        while(Q.size()!=0)
        {
            a=Q.peek();Q.remove();
            for(int i=0;i<dim;++i)
            {
                if(graph[a][i]=='1')
                {
                    if(seen[i]==0)
                    {
                    parent[i]=a;
                    //System.out.printf("%d , ",i);
                    level[i]=level[a]+1;
                    seen[i]=1;
                    Q.add(i);
                    }
                }
            }

        }
    }

    static void dfs(int a)
    {
        //System.out.printf("%d , ",a);
        seen[a]=1;

        for(int i=0;i<dim;++i)
        {
            if(graph[a][i]=='1')
            {
                if(seen[i]==0)
                {
                dfs(i);
                }
            }
        }
    }

    static void clear()
    {
        for(int i=0;i<dim;++i)seen[i]=0;
    }

    static void recordpath(int a,int b)
    {
        bfs(a);
        Stack<Integer> stk= new Stack<>();
        System.out.printf("\n1) Path from %d to %d is ,\n\t",a,b);
        int x=b;
        while(x!=a)
        {
            //System.out.printf(" %d , ",x);
            stk.push(x);
            x=parent[x];
        }
        stk.push(x);

        while(!stk.empty())
        {
            System.out.printf(" %d , ",stk.peek());
            stk.pop();
        }

        System.out.printf("\n2) distance from %d to %d is %d\n",a,b,level[b]);
        clear();
    }
    static void garbage(int a)
    {
        numbereddfs(a,-1);
        for(int i=0;i<dim;++i)
        {
            if(seen[i]==0)
                numbereddfs(i,-1);
        }
    }
    static void numbereddfs(int v,int u) //u is parent of v.
    {
        parent[v]=u;
        seen[v]=1;

        prenumber[v]=clock++;

        for(int i=0;i<dim;i++)
        {
            if(graph[v][i]=='1')
            {
                if(seen[i]==0)
                    numbereddfs(i,v);
            }
        }
        postnumber[v]=clock++;

    }

    static int noofcomponents()
    {
        clear();

        int c=0;
        for(int i=0;i<dim;++i)
        {
            if(seen[i]==0)
                {
                    c++;
                    dfs(i);

                }
        }



        return c;
    }

    static void edges()
    {
        System.out.println("\n3)");
        for(int i=0;i<dim;++i) //u (parent)
        {
            for(int j=0;j<dim;++j) // v (child)
            {

                if(graph[i][j]=='1')
                {
                    if(prenumber[i]<prenumber[j] && postnumber[i]>postnumber[j] && parent[j]==i)
                        edge[i][j]='t';

                    else if(prenumber[i]>prenumber[j] && postnumber[i]<postnumber[j])
                        edge[i][j]='b';

                    else if(prenumber[i]<prenumber[j] && postnumber[i]>postnumber[j])
                        edge[i][j]='f';

                    else if(prenumber[i]>prenumber[j] && postnumber[i]>postnumber[j])
                        edge[i][j]='c';

                }
                if(edge[i][j]!=0)
                System.out.printf("The edge %d,%d is a ",i,j);

                switch(edge[i][j])
                {
                    case 't':
                        System.out.println("treeedge");
                        break;
                    case 'b':
                        System.out.println("backedge");
                        break;
                    case 'f':
                        System.out.println("forwardedge");
                        break;
                    case 'c':
                        System.out.println("crossedge");
                }

                if(edge[i][j]=='b')hasloop=1;
            }
        }

    }

    static void equalize(boolean flag)
    {
        for(int i=0;i<dim;++i)
        {
            for(int j=0;j<dim;++j)
            {
                if(flag)
                graph2[i][j]=graph[i][j];
                else
                graph[i][j]=graph2[i][j];
            }
        }
    }

    static void remove(int x)
    {
        for(int i=0;i<dim;++i)
        {
            graph[x][i]='0';
            graph[i][x]='0';
        }

    }

    static void articulationpoint()
    {
        equalize(true);

        int x=noofcomponents();
        System.out.println("6) ");
        for(int i=0;i<dim;++i)
        {
            remove(i);
            //display();
            int y=noofcomponents();
            equalize(false);
            if(y>x+1)
            {
                System.out.println("vertex "+i+" is an articulationpoint, increased connected components from "+x+" to "+(y-1));
            }
        }


    }

    static void bridge()
    {
        System.out.println("\n\n7)");
        int c = noofcomponents();
        for(int i=0;i<dim;++i)
        {
            for(int j=0;j<dim;++j)
            {
                if(graph[i][j]=='1')
                {

                    graph[i][j]='0';
                    //display();
                    int d=noofcomponents();
                    //System.out.println("befor deletion it was "+c+" components, now its "+d);
                    graph[i][j]='1';
                    if(d>c)
                    {

                        //System.out.println("befor deletion it was "+c+" components, now its "+d);
                        System.out.println(" edge "+i+ ","+j+" is a bridge");
                    }
                }

            }
        }


    }


	public static void main(String[] args)
	{

	    Scanner sc=new Scanner(System.in);
    	System.out.printf("Enter the no of vertices in the graph\n");

        dim=sc.nextInt();

        for(int i=0;i<dim;++i)
        for(int j=0;j<dim;++j)
            graph[i][j]='0';


        System.out.printf("Enter no of edges between vertices\n");

        int m;
        m=sc.nextInt();


        for(int i=0;i<m;++i)
        {
            int a,b;
            a=sc.nextInt();
            b=sc.nextInt();
            graph[a][b]='1';
            //graph[b][a]='1';
        }

		display();

		int a,b;
		System.out.println("enter the source and destination vertex for path ");

		a=sc.nextInt();
		b=sc.nextInt();

		recordpath(a,b);

		garbage(a);
		/*
		for(int i=0;i<dim;++i)
		{
		    System.out.printf(" vertex %d:%d-%d , ",i,prenumber[i],postnumber[i]);
		}*/
		edges();

		System.out.println(hasloop==1?"\n4) It has a loop\n":"\n4) It doesn't have a loop\n");

		int c= noofcomponents();
		System.out.println("5) Graph has "+c+" no. of components\n");
		articulationpoint();
	    //display();
		bridge();
	}
}
