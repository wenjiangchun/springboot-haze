package com.haze.vsail.bus.entity;

import com.haze.core.jpa.entity.AbstractLoginDeletedEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

/**
 * 车辆配置信息
 */
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
@Table(name = "v_bus_model")
public class BusModel extends AbstractLoginDeletedEntity<Long> {

    private String name;
    private BusEngine busEngine;
    private String leftUrl;
    private ConfigPos leftPos;
    private Integer leftCount;
    private String rightUrl;
    private ConfigPos rightPos;
    private Integer rightCount;
    private String decorator;
    private String led;
    private String controlPack;
    private String controlSeat;
    private String dashboard;
    private String passengerSeat;
    private String handrail;
    private String floor;
    private String floorDeco;
    private String fencePlate;
    private String windWindow;
    private String sideWindow;
    private String topWindow;
    private String passengerDoor;
    private String mirror;
    private String wiper;
    private String motor;
    private String airFilter;
    private String coldPipe;
    private String naturalGas;
    private String intellijTemp;
    private String accelerator;
    private String autoOutfire;
    private String autoSpeed;
    private String busPack;
    private String suspension;
    private String ecas;
    private String diskBrake;
    private String autoLub;
    private String steering;
    private String tyre;
    private String busEbs;
    private String busAbs;
    private String airBrake;
    private String dryer;
    private String otherBrake;
    private String drainValve;
    private String alumWheel;
    private String gen;
    private String airCond;
    private String warmAir;
    private String electControl;
    private String can;
    private String wire;
    private String wireHarness;
    private String battery;
    private String electSystem;
    private String oil;
    private String hvCabel;
    private String electCond;
    private String towControl;
    private String bms;
    private String electConverter;
    private String electMeter;
    private String insuDetector;
    private String electCollector;
    private String defroster;
    private String compressor;
    private String hydrPump;
    private String insuHot;
    private String batteryOutfire;
    private String busOutfire;
    private String driveShaft;
    private String insurance;
    private String pullRod;
    private String absorber;
    private String autifog;
    private String coldPump;
    private String annoAntifog;
    private String innerOutfire;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "engine_id")
    public BusEngine getBusEngine() {
        return busEngine;
    }

    public void setBusEngine(BusEngine busEngine) {
        this.busEngine = busEngine;
    }

    public String getLeftUrl() {
        return leftUrl;
    }

    public void setLeftUrl(String leftUrl) {
        this.leftUrl = leftUrl;
    }

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    public ConfigPos getLeftPos() {
        return leftPos;
    }

    public void setLeftPos(ConfigPos leftPos) {
        this.leftPos = leftPos;
    }

    public Integer getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(Integer leftCount) {
        this.leftCount = leftCount;
    }

    public String getRightUrl() {
        return rightUrl;
    }

    public void setRightUrl(String rightUrl) {
        this.rightUrl = rightUrl;
    }

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    public ConfigPos getRightPos() {
        return rightPos;
    }

    public void setRightPos(ConfigPos rightPos) {
        this.rightPos = rightPos;
    }

    public Integer getRightCount() {
        return rightCount;
    }

    public void setRightCount(Integer rightCount) {
        this.rightCount = rightCount;
    }

    public String getDecorator() {
        return decorator;
    }

    public void setDecorator(String decorator) {
        this.decorator = decorator;
    }

    public String getLed() {
        return led;
    }

    public void setLed(String led) {
        this.led = led;
    }

    public String getControlPack() {
        return controlPack;
    }

    public void setControlPack(String controlPack) {
        this.controlPack = controlPack;
    }

    public String getControlSeat() {
        return controlSeat;
    }

    public void setControlSeat(String controlSeat) {
        this.controlSeat = controlSeat;
    }

    public String getDashboard() {
        return dashboard;
    }

    public void setDashboard(String dashboard) {
        this.dashboard = dashboard;
    }

    public String getPassengerSeat() {
        return passengerSeat;
    }

    public void setPassengerSeat(String passengerSeat) {
        this.passengerSeat = passengerSeat;
    }

    public String getHandrail() {
        return handrail;
    }

    public void setHandrail(String handrail) {
        this.handrail = handrail;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getFloorDeco() {
        return floorDeco;
    }

    public void setFloorDeco(String floorDeco) {
        this.floorDeco = floorDeco;
    }

    public String getFencePlate() {
        return fencePlate;
    }

    public void setFencePlate(String fencePlate) {
        this.fencePlate = fencePlate;
    }

    public String getWindWindow() {
        return windWindow;
    }

    public void setWindWindow(String windWindow) {
        this.windWindow = windWindow;
    }

    public String getSideWindow() {
        return sideWindow;
    }

    public void setSideWindow(String sideWindow) {
        this.sideWindow = sideWindow;
    }

    public String getTopWindow() {
        return topWindow;
    }

    public void setTopWindow(String topWindow) {
        this.topWindow = topWindow;
    }

    public String getPassengerDoor() {
        return passengerDoor;
    }

    public void setPassengerDoor(String passengerDoor) {
        this.passengerDoor = passengerDoor;
    }

    public String getMirror() {
        return mirror;
    }

    public void setMirror(String mirror) {
        this.mirror = mirror;
    }

    public String getWiper() {
        return wiper;
    }

    public void setWiper(String wiper) {
        this.wiper = wiper;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getAirFilter() {
        return airFilter;
    }

    public void setAirFilter(String airFilter) {
        this.airFilter = airFilter;
    }

    public String getColdPipe() {
        return coldPipe;
    }

    public void setColdPipe(String coldPipe) {
        this.coldPipe = coldPipe;
    }

    public String getNaturalGas() {
        return naturalGas;
    }

    public void setNaturalGas(String naturalGas) {
        this.naturalGas = naturalGas;
    }

    public String getIntellijTemp() {
        return intellijTemp;
    }

    public void setIntellijTemp(String intellijTemp) {
        this.intellijTemp = intellijTemp;
    }

    public String getAccelerator() {
        return accelerator;
    }

    public void setAccelerator(String accelerator) {
        this.accelerator = accelerator;
    }

    public String getAutoOutfire() {
        return autoOutfire;
    }

    public void setAutoOutfire(String autoOutfire) {
        this.autoOutfire = autoOutfire;
    }

    public String getAutoSpeed() {
        return autoSpeed;
    }

    public void setAutoSpeed(String autoSpeed) {
        this.autoSpeed = autoSpeed;
    }

    public String getBusPack() {
        return busPack;
    }

    public void setBusPack(String busPack) {
        this.busPack = busPack;
    }

    public String getSuspension() {
        return suspension;
    }

    public void setSuspension(String suspension) {
        this.suspension = suspension;
    }

    public String getEcas() {
        return ecas;
    }

    public void setEcas(String ecas) {
        this.ecas = ecas;
    }

    public String getDiskBrake() {
        return diskBrake;
    }

    public void setDiskBrake(String diskBrake) {
        this.diskBrake = diskBrake;
    }

    public String getAutoLub() {
        return autoLub;
    }

    public void setAutoLub(String autoLub) {
        this.autoLub = autoLub;
    }

    public String getSteering() {
        return steering;
    }

    public void setSteering(String steering) {
        this.steering = steering;
    }

    public String getTyre() {
        return tyre;
    }

    public void setTyre(String tyre) {
        this.tyre = tyre;
    }

    public String getBusEbs() {
        return busEbs;
    }

    public void setBusEbs(String busEbs) {
        this.busEbs = busEbs;
    }

    public String getBusAbs() {
        return busAbs;
    }

    public void setBusAbs(String busAbs) {
        this.busAbs = busAbs;
    }

    public String getAirBrake() {
        return airBrake;
    }

    public void setAirBrake(String airBrake) {
        this.airBrake = airBrake;
    }

    public String getDryer() {
        return dryer;
    }

    public void setDryer(String dryer) {
        this.dryer = dryer;
    }

    public String getOtherBrake() {
        return otherBrake;
    }

    public void setOtherBrake(String otherBrake) {
        this.otherBrake = otherBrake;
    }

    public String getDrainValve() {
        return drainValve;
    }

    public void setDrainValve(String drainValve) {
        this.drainValve = drainValve;
    }

    public String getAlumWheel() {
        return alumWheel;
    }

    public void setAlumWheel(String alumWheel) {
        this.alumWheel = alumWheel;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public String getAirCond() {
        return airCond;
    }

    public void setAirCond(String airCond) {
        this.airCond = airCond;
    }

    public String getWarmAir() {
        return warmAir;
    }

    public void setWarmAir(String warmAir) {
        this.warmAir = warmAir;
    }

    public String getElectControl() {
        return electControl;
    }

    public void setElectControl(String electControl) {
        this.electControl = electControl;
    }

    public String getCan() {
        return can;
    }

    public void setCan(String can) {
        this.can = can;
    }

    public String getWire() {
        return wire;
    }

    public void setWire(String wire) {
        this.wire = wire;
    }

    public String getWireHarness() {
        return wireHarness;
    }

    public void setWireHarness(String wireHarness) {
        this.wireHarness = wireHarness;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getElectSystem() {
        return electSystem;
    }

    public void setElectSystem(String electSystem) {
        this.electSystem = electSystem;
    }

    public String getOil() {
        return oil;
    }

    public void setOil(String oil) {
        this.oil = oil;
    }

    public String getHvCabel() {
        return hvCabel;
    }

    public void setHvCabel(String hvCabel) {
        this.hvCabel = hvCabel;
    }

    public String getElectCond() {
        return electCond;
    }

    public void setElectCond(String electCond) {
        this.electCond = electCond;
    }

    public String getTowControl() {
        return towControl;
    }

    public void setTowControl(String towControl) {
        this.towControl = towControl;
    }

    public String getBms() {
        return bms;
    }

    public void setBms(String bms) {
        this.bms = bms;
    }

    public String getElectConverter() {
        return electConverter;
    }

    public void setElectConverter(String electConverter) {
        this.electConverter = electConverter;
    }

    public String getElectMeter() {
        return electMeter;
    }

    public void setElectMeter(String electMeter) {
        this.electMeter = electMeter;
    }

    public String getInsuDetector() {
        return insuDetector;
    }

    public void setInsuDetector(String insuDetector) {
        this.insuDetector = insuDetector;
    }

    public String getElectCollector() {
        return electCollector;
    }

    public void setElectCollector(String electCollector) {
        this.electCollector = electCollector;
    }

    public String getDefroster() {
        return defroster;
    }

    public void setDefroster(String defroster) {
        this.defroster = defroster;
    }

    public String getCompressor() {
        return compressor;
    }

    public void setCompressor(String compressor) {
        this.compressor = compressor;
    }

    public String getHydrPump() {
        return hydrPump;
    }

    public void setHydrPump(String hydrPump) {
        this.hydrPump = hydrPump;
    }

    public String getInsuHot() {
        return insuHot;
    }

    public void setInsuHot(String insuHot) {
        this.insuHot = insuHot;
    }

    public String getBatteryOutfire() {
        return batteryOutfire;
    }

    public void setBatteryOutfire(String batteryOutfire) {
        this.batteryOutfire = batteryOutfire;
    }

    public String getBusOutfire() {
        return busOutfire;
    }

    public void setBusOutfire(String busOutfire) {
        this.busOutfire = busOutfire;
    }

    public String getDriveShaft() {
        return driveShaft;
    }

    public void setDriveShaft(String driveShaft) {
        this.driveShaft = driveShaft;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getPullRod() {
        return pullRod;
    }

    public void setPullRod(String pullRod) {
        this.pullRod = pullRod;
    }

    public String getAbsorber() {
        return absorber;
    }

    public void setAbsorber(String absorber) {
        this.absorber = absorber;
    }

    public String getAutifog() {
        return autifog;
    }

    public void setAutifog(String autifog) {
        this.autifog = autifog;
    }

    public String getColdPump() {
        return coldPump;
    }

    public void setColdPump(String coldPump) {
        this.coldPump = coldPump;
    }

    public String getAnnoAntifog() {
        return annoAntifog;
    }

    public void setAnnoAntifog(String annoAntifog) {
        this.annoAntifog = annoAntifog;
    }

    public String getInnerOutfire() {
        return innerOutfire;
    }

    public void setInnerOutfire(String innerOutfire) {
        this.innerOutfire = innerOutfire;
    }

    @Override
    public String toString() {
        return "BusEngine{" +
                "name='" + name + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                '}';
    }
}