package com.cfyifei.gui.tileentitys;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;




import com.cfyifei.gui.blocks.BlockNt;
import com.cfyifei.gui.blocks.ModGui;
import com.cfyifei.item.ModItem;


public class TileEntityNt extends TileEntity implements IInventory{
	private ItemStack stack[] = new ItemStack[6];
	public int tableBurnTime = 0;
	public int maxBurnTime = 0;
    public int currentItemBurnTime;
    public int furnaceCookTime;
	private String field_145958_o;
	public int water;
	public ItemStack cai;


	@Override
    public void updateEntity() {
		
		boolean flag = this.tableBurnTime > 0;
	        boolean flag1 = false;

	        
	        if (!this.worldObj.isRemote)
	        {
	        	cai();
	    

	            if (cai != null && this.water == 8 && canSmelt())
	            {
	                ++this.furnaceCookTime;

	                if (this.furnaceCookTime == 3600)
	                {
	                    this.furnaceCookTime = 0;
	                    this.smeltItem();
	                    flag1 = true;
	                }
	            }
	            else
	            {
	                this.furnaceCookTime = 0;
	            }

	        if (flag1)
	        {
	            this.markDirty();
	        }
	        if (stack[3] != null){
	        	if(water != 8){
	 
	        		if(stack[3].getItem().equals(Items.potionitem)){

	        			stack[3] = new ItemStack(Items.glass_bottle);
	        			++water;	      
	        			
	        		}	
	        	}
	        }
	        }
	}
		 

	@Override
	public int getSizeInventory() {
		
		return stack.length;

	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		
		return stack[var1];
	}

	@Override
    public ItemStack decrStackSize(int par1, int par2) {
            // TODO Auto-generated method stub
            if (this.stack[par1] != null)
    {
        ItemStack var3;

        if (this.stack[par1].stackSize <= par2)
        {
            var3 = this.stack[par1];
            this.stack[par1] = null;
            return var3;
        }
        else
        {
            var3 = this.stack[par1].splitStack(par2);

            if (this.stack[par1].stackSize == 0)
            {
                this.stack[par1] = null;
            }

            return var3;
        }
    }
    else
    {
        return null;
    }
    }

	@Override
	 public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.stack[par1] != null)
        {
            ItemStack itemstack = this.stack[par1];
            this.stack[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
        this.stack[var1] = var2;
        if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
        {
                var2.stackSize = this.getInventoryStackLimit();
        }
	}

	@Override
	public String getInventoryName() {
		
		return "Mill";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		
		return true;
	}

	@Override
	public void openInventory() {
		
		
	}

	@Override
	public void closeInventory() {
		
		
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		
		return false;
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
        this.stack = new ItemStack[this.getSizeInventory()];
        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.getCompoundTagAt(var3);
            byte var5 = var4.getByte("Slot");
            if (var5 >= 0 && var5 < this.stack.length)
            {
                this.stack[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
        this.tableBurnTime = par1NBTTagCompound.getShort("tableBurnTime");
        this.furnaceCookTime = par1NBTTagCompound.getShort("furnaceCookTime");
        this.water = par1NBTTagCompound.getShort("water");
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setShort("tableBurnTime", (short)this.tableBurnTime);
        par1NBTTagCompound.setShort("furnaceCookTime", (short)this.furnaceCookTime);
        par1NBTTagCompound.setShort("water", (short)this.water);
        NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.stack.length; ++var3)
        {
            if (this.stack[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.stack[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        par1NBTTagCompound.setTag("Items", var2);
       
    }
    
    public static int getItemBurnTime(ItemStack par0ItemStack)
    {
        if (par0ItemStack == null)
        {
            return 0;
        }
        else
        {
            net.minecraft.item.Item item = par0ItemStack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
            {
            	Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab)
                {
                    return 150;
                }

                if (block.getMaterial() == Material.wood)
                {
                    return 300;
                }
                if (block == Blocks.coal_block)
                {
                    return 16000;
                }
            }
            if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item == Items.stick) return 100;
            if (item == Items.coal) return 1600;
            if (item == Items.lava_bucket) return 20000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if (item == Items.blaze_rod) return 2400;
            return GameRegistry.getFuelValue(par0ItemStack);
        }
    }
    
    private boolean canSmelt()
    {
    	if(this.stack[5] != null){
       	 if (!this.stack[5].isItemEqual(cai)) return false;
         int result = stack[5].stackSize + cai.stackSize;
         return result <= getInventoryStackLimit() && result <= this.stack[5].getMaxStackSize(); 
    	}

    	if (this.stack[0] != null && this.stack[1] != null && this.stack[2] != null){ 
            if (cai == null) return false;
            return true;
    	}
		return false;
    }

   
    public void smeltItem()
    {

            if (this.stack[5] == null)
            {
                this.stack[5] = cai.copy();
            }
            else if (this.stack[5].getItem() == cai.getItem())
            {
                this.stack[5].stackSize += cai.stackSize; // Forge BugFix: Results may have multiple items
            }

            --this.stack[0].stackSize;
            --this.stack[1].stackSize;
            --this.stack[2].stackSize;

            if (this.stack[0].stackSize <= 0)
            {
                this.stack[0] = null;
            }
            if (this.stack[1].stackSize <= 0)
            {
                this.stack[1] = null;
            }
            if (this.stack[2].stackSize <= 0)
            {
                this.stack[2] = null;
            }
        water =  0;;
    }



	public int getWater() {
		return this.water * 7;
	}
	public void cai() {
       if(stack[0] != null && stack[1] != null && stack[2] != null){
    	   if(stack[0].getItem() == ModItem.ItemDouzi && stack[1].getItem() == ModItem.ItemMianfen && stack[2].getItem() == ModItem.ItemYan) cai = new ItemStack(ModItem.ItemJiangyou,8);
    	   if(stack[0].getItem() == ModItem.ItemDouzi && stack[1].getItem() == ModItem.ItemDouzi && stack[2].getItem() == Items.sugar) cai = new ItemStack(ModItem.ItemCu,8);
    	   if(stack[0].getItem() == ModItem.ItemPutao && stack[1].getItem() == ModItem.ItemPutao && stack[2].getItem() == ModItem.ItemPutao) cai = new ItemStack(ModItem.ItemPutaojiu,3);
    	   if(stack[0].getItem() == ModItem.ItemPutaojiu && stack[1].getItem() == ModItem.ItemPutaojiu && stack[2].getItem() == ModItem.ItemPutaojiu) cai = new ItemStack(ModItem.ItemHongjiu,9);
    	   if(stack[0].getItem() == ModItem.ItemDami && stack[1].getItem() == ModItem.ItemDami && stack[2].getItem() == ModItem.ItemDami) cai = new ItemStack(ModItem.ItemBaijiu,3);
    	   if(stack[0].getItem() == Items.apple && stack[1].getItem() == Items.apple && stack[2].getItem() == Items.apple) cai = new ItemStack(ModItem.ItemPingguojiu,3);
    	   if(stack[0].getItem() == Items.golden_apple && stack[1].getItem() == Items.golden_apple && stack[2].getItem() == Items.golden_apple) cai = new ItemStack(ModItem.ItemJinpingguojiu,3);
      	   if(stack[0].getItem() == ModItem.ItemJinputao && stack[1].getItem() == ModItem.ItemJinputao && stack[2].getItem() == ModItem.ItemJinputao) cai = new ItemStack(ModItem.ItemJinputaojiu,3);
       }
       
       else{
    	   cai = null;
       }
	}


	public boolean isBurning() {
	return this.furnaceCookTime > 0;
	}
}
