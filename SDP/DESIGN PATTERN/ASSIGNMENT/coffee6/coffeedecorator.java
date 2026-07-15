public abstract class coffeedecorator implements coffee {
    
        protected coffee cf;

        public coffeedecorator(coffee cf)
        {
            this.cf=cf;
        }
        public String getDescription()
        {
            return cf.getDescription();
        }
        public int getCost()
        {
            return cf.getCost();
        }
}
