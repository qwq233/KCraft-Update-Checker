package net.zbx1425.playsoundtweak;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileEntitySpeaker extends TileEntity {
    public String soundname;
    public boolean constvol;
    public boolean previousRedstoneState;
    
    public TileEntitySpeaker() {
    	soundname = "minecraft:entity.minecart.inside";
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
    	//System.out.println("NBT Wrote!");
        super.writeToNBT(compound);
        compound.setString("soundname", this.soundname);
        compound.setBoolean("powered", this.previousRedstoneState);
        compound.setBoolean("constvol", this.constvol);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
    	//System.out.println("NBT Read!");
        super.readFromNBT(compound);
        this.soundname = compound.getString("soundname");
        this.previousRedstoneState = compound.getBoolean("powered");
        this.constvol = compound.getBoolean("constvol");
    }
    
    public NBTTagCompound getUpdateTag()
    {
    	//System.out.println("DataPacket Sent!");
        return this.writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket(){
    	//System.out.println("DataPacket Sent!");
        return new SPacketUpdateTileEntity(getPos(), 9, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
    	//System.out.println("DataPacket Received!");
        NBTTagCompound tag = pkt.getNbtCompound();
        readFromNBT(tag);
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
    }
    
    public void triggerNote(World worldIn, BlockPos posIn)
    {
        worldIn.addBlockEvent(posIn, CommonProxy.speakerBlock, 0, 0);
    }
}
